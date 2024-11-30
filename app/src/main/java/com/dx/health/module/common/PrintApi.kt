package com.dx.health.module.common

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintJob
import android.print.PrintManager
import android.print.pdf.PrintedPdfDocument
import android.webkit.WebView
import androidx.print.PrintHelper
import com.dx.health.R
import java.io.FileOutputStream
import java.io.IOException

/**
 * Author: Meng
 * Date: 2023/08/07
 * Modify: 2023/08/07
 * Desc:
 * https://developer.android.google.cn/training/printing/custom-docs?hl=zh-cn
 */
class PrintApi {

    /**
     * SCALE_MODE_FIT - 此选项用于调整图片大小，使整个图片显示在页面的可打印区域内。
     * SCALE_MODE_FILL - 此选项用于缩放图片，使其填充页面的整个可打印区域。选择此设置意味着图片上下或左右边缘的某些部分不会打印出来。如果您未设置缩放模式，则此选项为默认值。
     */
    fun printImg(activity: Activity) {
        activity.also { context ->
            PrintHelper(context).apply {
                scaleMode = PrintHelper.SCALE_MODE_FIT
            }.also { printHelper ->
                val bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_launcher_background)
                printHelper.printBitmap("droids.jpg - test print", bitmap)
            }
        }
    }

    fun printH5(activity: Activity, webView: WebView) {
        (activity.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val printJobs = ArrayList<PrintJob>()
            val jobName = "Print Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build()
            ).also { printJob ->
                // Save the job object for later status checking
                printJobs.add(printJob)
            }
        }
    }

    fun printOther(activity: Activity) {
        activity.also { context ->
            // Get a PrintManager instance
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            // Set job name, which will be displayed in the print queue
            val jobName = "Print Document"
            // Start a print job, passing in a PrintDocumentAdapter implementation
            // to handle the generation of a print document
            printManager.print(jobName, MyPrintDocumentAdapter(context), null)
        }
    }


    /**
     *
    onStart() - 在打印过程开始时调用一次。如果应用有任何一次性准备任务要执行，例如获取要打印的数据的快照，请在此处执行这些任务。无需在适配器中实现此方法。
    onLayout() - 每当用户更改影响输出的打印设置（例如不同的页面大小或页面方向）时调用，让应用有机会计算要打印的页面的布局。此方法至少必须返回打印文档预期的页数。
    onWrite() - 调用它来将打印的页面呈现为要打印的文件。在每次调用 onLayout() 之后，可能调用此方法一次或多次。
    onFinish() - 在打印过程结束时调用一次。如果您的应用有任何一次性拆解任务要执行，请在此处执行这些任务。无需在适配器中实现此方法。
     */
    class MyPrintDocumentAdapter(val context: Context): PrintDocumentAdapter() {
        private var pdfDocument: PrintedPdfDocument? = null
        private var totalPages = 9

        override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback?,
            extras: Bundle?
        ) {
            pdfDocument = newAttributes.let { PrintedPdfDocument(context, it) }

            // Respond to cancellation request
            if (cancellationSignal?.isCanceled == true) {
                callback?.onLayoutCancelled()
                return
            }

            // Compute the expected number of printed pages
            val pages = computePageCount(newAttributes)

            if (pages > 0) {
                // Return print information to print framework
                PrintDocumentInfo.Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(pages)
                    .build()
                    .also { info ->
                        // Content layout reflow is complete
                        callback?.onLayoutFinished(info, true)
                    }
            } else {
                // Otherwise report an error to the print framework
                callback?.onLayoutFailed("Page count calculation failed.")
            }
        }

        private fun computePageCount(printAttributes: PrintAttributes): Int {
            var itemsPerPage = 4 // default item count for portrait mode

            val pageSize = printAttributes.mediaSize
            if (pageSize != null) {
                if (!pageSize.isPortrait) {
                    // Six items per page in landscape orientation
                    itemsPerPage = 6
                }
            }

            // Determine number of print items
            val printItemCount: Int = getPrintItemCount()

            return Math.ceil((printItemCount / itemsPerPage.toDouble())).toInt()
        }

        private fun getPrintItemCount(): Int {
            return 0
        }

        override fun onWrite(
            pages: Array<out PageRange>?,
            destination: ParcelFileDescriptor?,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback?
        ) {
            // Iterate over each page of the document,
            // check if it's in the output range.
            for (i in 0 until totalPages) {
                // Check to see if this page is in the output range.
//                if (containsPage(pageRanges, i)) {
//                    // If so, add it to writtenPagesArray. writtenPagesArray.size()
//                    // is used to compute the next output page index.
//                    writtenPagesArray.append(writtenPagesArray.size(), i)
//                    pdfDocument?.startPage(i)?.also { page ->
//
//                        // check for cancellation
//                        if (cancellationSignal?.isCanceled == true) {
//                            callback.onWriteCancelled()
//                            pdfDocument?.close()
//                            pdfDocument = null
//                            return
//                        }
//
//                        // Draw page content for printing
//                        drawPage(page)
//
//                        // Rendering is complete, so page can be finalized.
//                        pdfDocument?.finishPage(page)
//                    }
//                }
            }

            // Write PDF document to file
            try {
                pdfDocument?.writeTo(FileOutputStream(destination?.fileDescriptor))
            } catch (e: IOException) {
                callback?.onWriteFailed(e.toString())
                return
            } finally {
                pdfDocument?.close()
                pdfDocument = null
            }
//            val writtenPages = computeWrittenPages()
//            // Signal the print framework the document is complete
//            callback.onWriteFinished(writtenPages)
        }

        private fun drawPage(page: PdfDocument.Page) {
            page.canvas.apply {

                // units are in points (1/72 of an inch)
                val titleBaseLine = 72f
                val leftMargin = 54f

                val paint = Paint()
                paint.color = Color.BLACK
                paint.textSize = 36f
                drawText("Test Title", leftMargin, titleBaseLine, paint)

                paint.textSize = 11f
                drawText("Test paragraph", leftMargin, titleBaseLine + 25, paint)

                paint.color = Color.BLUE
                drawRect(100f, 100f, 172f, 172f, paint)
            }
        }

    }
}