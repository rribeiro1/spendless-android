package io.rafaelribeiro.spendless.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.wrapContentSize
import io.rafaelribeiro.spendless.MainActivity
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.presentation.widget.CreateTransactionWidget.WIDGET_INTENT_KEY

object CreateTransactionWidget : GlanceAppWidget() {
    const val WIDGET_INTENT_KEY = "create_transaction_widget"
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }
}

class CreateTransactionWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CreateTransactionWidget

}

@Composable
fun WidgetContent() {
    val widgetKey = ActionParameters.Key<Boolean>(WIDGET_INTENT_KEY)
    Box(
        modifier = GlanceModifier
            .fillMaxWidth()
            .clickable(actionStartActivity<MainActivity>(actionParametersOf(widgetKey to true))),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = GlanceModifier.wrapContentSize(),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Vertical.Top
        ) {
            Image(
                provider = ImageProvider(R.drawable.widget_preview),
                contentDescription = null,
            )
        }
    }
}
