package com.ql.health.module.common.network

import com.ql.health.module.common.network.body.OnProgressListener
import com.ql.health.module.common.network.interceptor.LogInterceptor
import com.ql.health.module.common.network.interceptor.ProgressInterceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Author: Meng
 * Date: 2023/08/03
 * Modify: 2023/08/03
 * Desc:
 */
class FileRequest {
    companion object {
        private val MEDIA_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
        private val MEDIA_PNG = "image/png".toMediaType()


        private fun client(listener: OnProgressListener): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(ProgressInterceptor(listener))
                .addInterceptor(LogInterceptor())
                .build()
        }

        private fun getMediaType(name: String): MediaType {
            return when(name) {
                ".png", ".jpg" -> MEDIA_PNG
                ".txt" -> MEDIA_MARKDOWN
                else -> MEDIA_MARKDOWN
            }
        }

        fun upload(path: String, listener: OnProgressListener) {

            val file = File(path).asRequestBody(getMediaType(path))

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "You Logo")
                .addFormDataPart("image", "name.png",file)
                .build()


//            val requestFile = file.asRequestBody(media.toMediaTypeOrNull())
//            val body: Part = Part.createFormData("file", file.name, requestFile)
//            val tag = "SD3123123".toRequestBody("text/plain".toMediaTypeOrNull())
//        val fileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//        val requestFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
//        val body: Part = Part.createFormData("file", file.name, requestFile)
//        List<MultipartBody.Part> parts = new ArrayList<>();
//        MultipartBody.Part file1 = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
//        MultipartBody.Part file2 = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
//        parts.add(file1);
//        parts.add(file2);

//        Map<String, RequestBody> map = new HashMap<>();
//        map.put(String.format("file\"; filename=\"" + file.getName()), RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file));
//        map.put(String.format("file\"; filename=\"" + file.getName()), RequestBody.create("image/jpg".toMediaTypeOrNull(), file)); // 图片名称不能包含中文字符
//        map.put("tag", RequestBody.create("text/plain".toMediaTypeOrNull(), "SD3123123"));

            val request = Request.Builder()
                .header("Authorization", "Client-ID")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build()

            client(listener).newCall(request).enqueue(object : OKCallback3<Any>() {

            })
        }
    }
}