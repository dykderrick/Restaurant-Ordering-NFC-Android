package com.app.nfc.activity

import android.graphics.PorterDuff
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.nfc.R
import com.app.nfc.base.BaseActivity
import com.app.nfc.fragment.HomeFragment
import com.app.nfc.fragment.PaginationFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var tabSelectedPosition = 0
    override fun setLayout(): Int {
        return R.layout.activity_main
    }

    override fun InitView() {
        setBtnView(0)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.rlHome -> {
                if (tabSelectedPosition != 0) {
                    tabSelectedPosition = 0
                    setBtnView(0)
                }
            }
            R.id.rlLocation -> {
                if (tabSelectedPosition != 1) {
                    tabSelectedPosition = 1
                    setBtnView(1)
                }
            }
            R.id.rlSetting -> {
                if (tabSelectedPosition != 2) {
                    tabSelectedPosition = 2
                    setBtnView(2)
                }
            }
            R.id.rlHeart -> {
                if (tabSelectedPosition != 3) {
                    tabSelectedPosition = 3
                    setBtnView(3)
                }
            }
            R.id.rlUser -> {
                if (tabSelectedPosition != 4) {
                    tabSelectedPosition = 4
                    setBtnView(4)
                }
            }
        }
    }

    private fun setBtnView(pagePos: Int) {

        ivHome.setColorFilter(ContextCompat.getColor(this, R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN)
        ivLocation.setColorFilter(ContextCompat.getColor(this, R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN)
        ivHeart.setColorFilter(ContextCompat.getColor(this, R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN)
        ivUser.setColorFilter(ContextCompat.getColor(this, R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN)
        ivSetting.setColorFilter(ContextCompat.getColor(this, R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN)

//        txtHome1.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected_color))

        when (pagePos) {
            0 -> {
                ivHome.setColorFilter(ContextCompat.getColor(this, R.color.tab_selected_color), PorterDuff.Mode.SRC_IN)
                 fragmentLoad(HomeFragment())
            }
            1 -> {
                ivLocation.setColorFilter(ContextCompat.getColor(this, R.color.tab_selected_color), PorterDuff.Mode.SRC_IN)
                fragmentLoad(PaginationFragment())
            }
            2 -> {
                ivSetting.setColorFilter(ContextCompat.getColor(this, R.color.tab_selected_color), PorterDuff.Mode.SRC_IN)
                fragmentLoad(HomeFragment())
            }
            3 -> {
                ivHeart.setColorFilter(ContextCompat.getColor(this, R.color.tab_selected_color), PorterDuff.Mode.SRC_IN)
                fragmentLoad(HomeFragment())
            }
            4 -> {
                ivUser.setColorFilter(ContextCompat.getColor(this, R.color.tab_selected_color), PorterDuff.Mode.SRC_IN)
                fragmentLoad(HomeFragment())
            }
        }
    }

    private fun fragmentLoad(mFragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, mFragment)
        fragmentTransaction.commit()
//        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if(tabSelectedPosition>0){
            tabSelectedPosition=0
            setBtnView(0)
        }else{
            finish()
        }
    }

}