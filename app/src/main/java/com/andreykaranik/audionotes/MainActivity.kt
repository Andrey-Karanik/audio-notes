package com.andreykaranik.audionotes

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.andreykaranik.audionotes.databinding.ActivityMainBinding
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val audioNotes = listOf<AudioNote>(
            AudioNote("Sound", "", 0),
            AudioNote("Music", "", 0),
            AudioNote("SuperSound", "", 0),
            AudioNote("Sound", "", 0),
            AudioNote("Music", "", 0),
            AudioNote("SuperSound", "", 0),
            AudioNote("Sound", "", 0),
            AudioNote("Music", "", 0),
            AudioNote("SuperSound", "", 0)
        )
        val adapter = AudioNoteItemAdapter(audioNotes)

        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (viewModel.isRecording.value) {
                    return
                }

                if (dy < 0 && viewModel.isRecordButtonGone.value) {
                    viewModel.showRecordButton()
                } else if (dy > 0 && !viewModel.isRecordButtonGone.value) {
                    viewModel.hideRecordButton()
                }
            }
        })

        binding.recordButton.setOnClickListener {
            if (viewModel.isRecording.value) {
                viewModel.stopRecording()
            } else {
                viewModel.startRecording()
            }
        }

        binding.recordTimeTextCardView.alpha = 0.0f
        binding.recordTimeTextCardView.scaleX = 0.0f
        binding.recordTimeTextCardView.scaleY = 0.0f

        binding.recordButton.alpha = 0.0f
        binding.recordButton.scaleX = 0.0f
        binding.recordButton.scaleY = 0.0f

        binding.shadeView.alpha = 0.0f

        val recordButtonWaveAnimator = AnimatorInflater.loadAnimator(this, R.animator.oa_record_button_wave) as ObjectAnimator
        recordButtonWaveAnimator.target = binding.recordButtonWaveView
        recordButtonWaveAnimator.doOnStart {
            binding.recordButtonWaveView.visibility = View.VISIBLE
        }
        recordButtonWaveAnimator.doOnEnd {
            binding.recordButtonWaveView.visibility = View.GONE
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isRecordButtonGone
                .onEach {
                    if (it) {
                        hideRecordButton()
                    } else {
                        showRecordButton()
                    }
                }
                .collect()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isRecording
                .onEach {
                    if (it) {
                        if (viewModel.isRecordButtonGone.value) {
                            viewModel.showRecordButton()
                        }
                        binding.recordButton.backgroundTintList = ColorStateList.valueOf(
                            MaterialColors.getColor(this@MainActivity, com.google.android.material.R.attr.colorSecondary, Color.BLACK))
                        binding.recordButton.setIconResource(R.drawable.ic_round_stop_24)
                        binding.recordTimeTextCardView.animate()
                            .setInterpolator(DecelerateInterpolator())
                            .setDuration(250)
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .alpha(1.0f)
                            .withStartAction {
                                binding.recordTimeTextCardView.visibility = View.VISIBLE
                            }
                        binding.shadeView.animate()
                            .setInterpolator(DecelerateInterpolator())
                            .setDuration(250)
                            .alpha(0.1f)
                            .withStartAction {
                                binding.shadeView.visibility = View.VISIBLE
                            }
                        recordButtonWaveAnimator.start()
                    } else {
                        binding.recordButton.backgroundTintList = ColorStateList.valueOf(
                            MaterialColors.getColor(this@MainActivity, androidx.constraintlayout.widget.R.attr.colorPrimary, Color.BLACK))
                        binding.recordButton.setIconResource(R.drawable.ic_round_mic_24)
                        binding.recordTimeTextCardView.animate()
                            .setInterpolator(AccelerateInterpolator())
                            .setDuration(250)
                            .scaleX(0.0f)
                            .scaleY(0.0f)
                            .alpha(0.0f)
                            .withEndAction {
                                binding.recordTimeTextCardView.visibility = View.GONE
                            }
                        binding.shadeView.animate()
                            .setInterpolator(DecelerateInterpolator())
                            .setDuration(250)
                            .alpha(0.0f)
                            .withEndAction {
                                binding.shadeView.visibility = View.GONE
                            }
                        recordButtonWaveAnimator.end()
                    }
                }
                .collect()
        }
    }

    private fun showRecordButton() {
        binding.recordButton.animate()
            .setInterpolator(DecelerateInterpolator())
            .setDuration(250)
            .translationY(0.0f)
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1.0f)
            .withStartAction {
                binding.recordButton.visibility = View.VISIBLE
            }
    }

    private fun hideRecordButton() {
        binding.recordButton.animate()
            .setInterpolator(AccelerateInterpolator())
            .setDuration(250)
            .translationY(binding.recordButton.height.toFloat())
            .scaleX(0.0f)
            .scaleY(0.0f)
            .alpha(0.0f)
            .withEndAction {
                binding.recordButton.visibility = View.GONE
            }
    }
}