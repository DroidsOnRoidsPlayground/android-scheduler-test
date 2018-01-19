package pl.droidsonroids.schedulertest;

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.TestScheduler
import java.util.*
import java.util.Calendar.AM
import java.util.Calendar.AM_PM
import java.util.concurrent.TimeUnit

class ClockPresenterTest {

    private var testScheduler = TestScheduler()

    @Mock
    private lateinit var view: ClockView
    @Mock
    private lateinit var calendar: Calendar

    @InjectMocks
    private lateinit var presenter: ClockPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaHooks.setOnIOScheduler { testScheduler }
        RxJavaHooks.setOnComputationScheduler { testScheduler }

        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler() = testScheduler
        })
    }

    @Test
    fun `should set current time on clock`() {
        whenever(calendar.time).thenReturn(Date(1516087347505L))
        whenever(calendar.timeZone).thenReturn(TimeZone.getTimeZone("Europe/London"))
        whenever(calendar.get(AM_PM)).thenReturn(AM)

        presenter.enterLayout()
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(view).show()
        verify(view).setTime("07:22")
    }

}