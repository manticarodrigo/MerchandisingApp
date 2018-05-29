package com.cad.ooqiadev.cadcheck_in.utils

import android.content.Context
import android.util.Log
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetAddress

class FTP {

    private var server = ""
    private val port = 21
    private var user = ""
    private var password = ""
    private var ftpClient: FTPClient? = null
    private var isFtpClientOpened = false
    private var context: Context? = null;

    constructor(context: Context) {
        this.context = context
    }

    // region Protected Methods
    protected fun openFTPConnection(): Result {
        val result = Result()
        val u = Helpers()

        this.server = u.getPreferenceValue("ftpHost", this.context!!);
        this.user = u.getPreferenceValue("ftpUser",this.context!!);
        this.password = u.getPreferenceValue("ftpPassword", this.context!!)

        ftpClient = FTPClient()
        try {

            ftpClient!!.setConnectTimeout(10000)
            if (server.contains("127.0.0.1") || server.contains("localhost")) {
                result.success = false
                result.message = "Servidor remoto no permitido."
            } else {
                ftpClient!!.connect(InetAddress.getByName(server))
                ftpClient!!.login(user, password)
                val reply = ftpClient!!.getReplyCode()

                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient!!.disconnect()
                    isFtpClientOpened = false
                    result.message = "Error de conexión a FTP."
                } else {
                    isFtpClientOpened = true
                    result.success = true
                    result.message = "Conexión exitosa."
                }
            }
        } catch (unknownHost: java.net.UnknownHostException) {
            Log.d("UnknownHostException", unknownHost.message, unknownHost)
            result.message = unknownHost.message
            result.exception = unknownHost
            isFtpClientOpened = false
        } catch (ioException: java.io.IOException) {
            Log.d("IOException", ioException.message, ioException)
            result.message = ioException.message
            result.exception = ioException
            isFtpClientOpened = false
        }

        return result
    }

    protected fun closeFtpConnection() {
        if (isFtpClientOpened) {
            try {
                ftpClient?.logout()
                isFtpClientOpened = false
            } catch (ioException: java.io.IOException) {
                Log.d("IOException", ioException.message, ioException)
                isFtpClientOpened = false
            }

        }
    }

    fun DownloadFile(remoteFile: String, localFile: String): Result {

        var result = Result()
        try {

            val openConnectionResult = openFTPConnection()

            if (openConnectionResult.success) {

                ftpClient!!.enterLocalPassiveMode()

                val file = FileOutputStream(localFile)

                result.success = ftpClient!!.retrieveFile(remoteFile, file)

                val reply = ftpClient!!.getReplyCode()

                if (!FTPReply.isPositiveCompletion(reply)) {
                    result.success = false
                    result.message = "Archivo no encontrado!"
                } else {
                    result.message = "Archivo descargado exitósamente! $localFile"
                }

                file.close()
            } else {
                result = openConnectionResult
            }

            closeFtpConnection()

        } catch (unknownHost: java.net.UnknownHostException) {
            result.message = unknownHost.message
            result.exception = unknownHost
            Log.d("UnknownHostException", unknownHost.message, unknownHost)
        } catch (ioException: java.io.IOException) {
            result.message = ioException.message
            result.exception = ioException
            Log.d("IOException", ioException.message, ioException)
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
            Log.d("Exception", ex.message, ex)
        }

        return result
    }

    fun UploadFile(remoteFile: String, localFile: String, Append: Boolean): Result {

        var result = Result()
        try {

            val openConnectionResult = openFTPConnection()

            if (openConnectionResult.success) {

                ftpClient!!.enterLocalPassiveMode()

                val file = FileInputStream(localFile)

                if (Append) {
                    result.success = ftpClient!!.appendFile(remoteFile, file)
                } else {
                    result.success  = ftpClient!!.storeFile(remoteFile, file)
                }

                val reply = ftpClient!!.getReplyCode()

                if (!FTPReply.isPositiveCompletion(reply)) {
                    result.success = false
                    result.message = "Archivo no encontrado!"
                } else {
                    result.message = "Archivo transferido exitósamente!"
                }

                file.close()
            } else {
                result = openConnectionResult
            }

            closeFtpConnection()

        } catch (unknownHost: java.net.UnknownHostException) {
            Log.d("UnknownHostException", unknownHost.message, unknownHost)
            result.message = unknownHost.message
            result.exception = unknownHost
        } catch (ioException: java.io.IOException) {
            Log.d("IOException", ioException.message, ioException)
            result.message = ioException.message
            result.exception = ioException
        }

        return result
    }

    fun testFTPConnection() : Result {

        val openConnectionResult = openFTPConnection()
        closeFtpConnection()

        return openConnectionResult

    }

}
