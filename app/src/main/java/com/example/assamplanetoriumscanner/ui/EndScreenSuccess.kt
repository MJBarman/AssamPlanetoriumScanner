package com.example.assamplanetoriumscanner.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.assamplanetoriumscanner.R

class EndScreenSuccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_screen_success)
        supportActionBar?.hide()


        val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
        animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                //after the animation ends it goes back to home-screen again
                val intent = Intent(this@EndScreenSuccess, MainScreen::class.java)
                startActivity(intent)
                finishAffinity()
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }

        })

        animationView.playAnimation()


    }

    override fun onBackPressed() {
        if (!shouldAllowBack()) {
            return
        }
        super.onBackPressed()
    }

    private fun shouldAllowBack(): Boolean {
        return false
    }
}