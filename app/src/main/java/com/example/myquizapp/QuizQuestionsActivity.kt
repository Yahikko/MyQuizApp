package com.example.myquizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myquizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), OnClickListener {

    private lateinit var viewBinding: ActivityQuizQuestionsBinding

    private var mUserName: String? = null

    private lateinit var mQuestionList: ArrayList<Question>
    private var mCurrentPosition = 1
    private var mSelectedPosition = 0
    private var mCorrectAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionList = Constants.getQuestions()

        viewBinding.tvOptionOne.setOnClickListener(this)
        viewBinding.tvOptionTwo.setOnClickListener(this)
        viewBinding.tvOptionThree.setOnClickListener(this)
        viewBinding.tvOptionFour.setOnClickListener(this)
        viewBinding.btnSubmit.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {

        val question = mQuestionList[mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionList.size) {
            viewBinding.btnSubmit.text = "FINISH"
        } else {
            viewBinding.btnSubmit.text = "SUBMIT"
        }

        viewBinding.ivFlag.setImageResource(question.image)
        viewBinding.progressBar.progress = mCurrentPosition
        viewBinding.tvProgress.text = "$mCurrentPosition/${viewBinding.progressBar.max}"
        viewBinding.tvQuestion.text = question.question
        viewBinding.tvOptionOne.text = question.optionOne
        viewBinding.tvOptionTwo.text = question.optionTwo
        viewBinding.tvOptionThree.text = question.optionThree
        viewBinding.tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()

        options.add(0, viewBinding.tvOptionOne)
        options.add(1, viewBinding.tvOptionTwo)
        options.add(2, viewBinding.tvOptionThree)
        options.add(3, viewBinding.tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.button_text_rectangle_style
            )
        }
    }

    private fun selectedOptionView(textView: TextView, selectedOptionNumber: Int) {

        defaultOptionsView()

        mSelectedPosition = selectedOptionNumber

        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(viewBinding.tvOptionOne, 1)
            }

            R.id.tv_option_two -> {
                selectedOptionView(viewBinding.tvOptionTwo, 2)
            }

            R.id.tv_option_three -> {
                selectedOptionView(viewBinding.tvOptionThree, 3)
            }

            R.id.tv_option_four -> {
                selectedOptionView(viewBinding.tvOptionFour, 4)
            }

            R.id.btn_submit -> {
                if (mSelectedPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionList.size -> {
                            setQuestion()
                        }

                        else -> {

                            val intent = Intent(this, ResultActivity::class.java)

                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList.size)

                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = mQuestionList[mCurrentPosition - 1]

                    if (question.correctAnswer != mSelectedPosition) {
                        answerView(mSelectedPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionList.size) {
                        viewBinding.btnSubmit.text = getString(R.string.finish)
                    } else {
                        viewBinding.btnSubmit.text = getString(R.string.go_to_next_question)
                    }

                    mSelectedPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {
            1 -> {
                viewBinding.tvOptionOne.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }

            2 -> {
                viewBinding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }

            3 -> {
                viewBinding.tvOptionThree.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }

            4 -> {
                viewBinding.tvOptionFour.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }
}