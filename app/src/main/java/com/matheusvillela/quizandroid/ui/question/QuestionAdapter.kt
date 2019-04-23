package com.matheusvillela.quizandroid.ui.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matheusvillela.quizandroid.R
import kotlinx.android.synthetic.main.item_answer.view.*

class QuestionAdapter(private val listener: AnswerSelectedListener) :
    RecyclerView.Adapter<QuestionAdapter.QuestionAdapterHolder>() {

    private var answers = listOf<String>()
    private var selectedAnswer : String? = null

    interface AnswerSelectedListener {
        fun onAnswerSelected(answer: String);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_answer, parent, false)
        return QuestionAdapterHolder(view)
    }

    override fun getItemCount(): Int = answers.size

    override fun onBindViewHolder(holder: QuestionAdapterHolder, position: Int) {
        val answer = answers[position]
        holder.questionTextView.text = answer
        holder.itemView.isSelected = answer == selectedAnswer
    }

    fun setAnswers(answers: List<String>) {
        this.answers = answers
        notifyDataSetChanged()
    }

    fun setCurrentAnswer(currentAnswer: String?) {
        selectedAnswer = currentAnswer
        notifyDataSetChanged()
    }

    inner class QuestionAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.itemAnswerText

        init {
            itemView.setOnClickListener {
                questionTextView.text.toString().let {
                    selectedAnswer = it
                    listener.onAnswerSelected(it)
                }
                notifyDataSetChanged()
            }
        }
    }
}