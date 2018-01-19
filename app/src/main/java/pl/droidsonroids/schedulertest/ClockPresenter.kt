package pl.droidsonroids.schedulertest;

import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ClockPresenter(val view: ClockView, private val calendar: Calendar) {

    private lateinit var clockSubscription: Subscription

    fun enterLayout() {
        view.show()
        clockSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ setCurrentTime() }, ::println)
    }

    private fun setCurrentTime() {
        val formattedTime = getCurrentTime(calendar)
        view.setTime(formattedTime)
    }

    private fun getCurrentTime(calendar: Calendar): String {
        val currentTime = calendar.time
        val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        timeFormat.timeZone = calendar.timeZone
        return timeFormat.format(currentTime)
    }

}

interface ClockView {
    fun show()
    fun setTime(formattedTime: String)
}

