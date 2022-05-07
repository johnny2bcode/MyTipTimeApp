package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //アプリのビュー階層のルート（binding.root）をコンテンツビューに指定
        setContentView(binding.root)

        //初期値表示
        displayTip(0.0)

        //ボタン押下時
        binding.calucateButton.setOnClickListener { calculateTip() }

        //サービス料金入力画面でのキーリスナー
        binding.constOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
    }

    /**
     * サービス料金入力時のキーボード操作リスナー
     *
     * @param view アプリ画面,
     * @param keyCode 押下されたキーの種類コード
     * @return true:ソフトからのEnterキーを検知（キーボードを非表示にする）, false:Enter以外のキーを検知（何もしない）
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    /**
     * サービス料金を計算する
     *
     * @return 何もしない(エラー検知時）
     */
    private fun calculateTip(){
        //サービス料金の取得(EditText型）
        val stringInTextFiled = binding.constOfServiceEditText.text.toString()

        //サービス料金が空だった場合を想定する
        val cost = stringInTextFiled.toDoubleOrNull()
        if(cost == null || cost == 0.0 ){
            //チップ合計金額をクリアする
            displayTip(0.0)
            return
        }

        //チップの割合を取得
        val tipPercentage = when(binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            R.id.option_fifteen_percent -> 0.15
            else -> 0.15
        }

        //チップを計算
        // (チップ = 料金 * チップの割合）
        var tip = tipPercentage * cost

        //端数切り上げの設定がある場合は、切り上げを実効
        //val roundUp = binding.roundUpSwitch.isChecked
        if( binding.roundUpSwitch.isChecked) {
            tip = ceil(tip)
        }
        //通貨設定
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.result_tip_amount, formattedTip)

    }

    /**
     * 表示するチップ料金を任意の金額で指定する
     * （初期表示時またはエラー表示の時に使用）
     *
     * @param tip 表示したい料金
     */
    private fun displayTip(tip : Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.result_tip_amount, formattedTip)
    }

}