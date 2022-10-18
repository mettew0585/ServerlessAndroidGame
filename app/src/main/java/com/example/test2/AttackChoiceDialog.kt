
package com.example.test2


import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView

class AttackChoiceDialog (context: Context) {
    private val dlg = Dialog(context)
    private lateinit var lblDesc: TextView
    private lateinit var OKBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var listener: AtackChoiceDialogOKClickedListener

    fun start(content: String) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.activity_attack_choice)
        dlg.setCancelable(false)

        lblDesc = dlg.findViewById(R.id.content)
        lblDesc.text = content
        OKBtn = dlg.findViewById(R.id.ok_btn)
        OKBtn.setOnClickListener {
            listener.onOKClicked("확인을 눌렀습니다")
            dlg.dismiss()
        }
        cancelBtn = dlg.findViewById(R.id.cancel_btn)
        cancelBtn.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object : AtackChoiceDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }

    interface AtackChoiceDialogOKClickedListener {
        fun onOKClicked(content: String)
    }
}