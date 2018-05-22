package com.cad.ooqiadev.cadcheck_in

import android.util.Log
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileOutputStream
import java.net.InetAddress

class FTP {

    private val server = "192.168.86.67"
    private val port = 21
    private val user = "shared"
    private val password = "ooqiadev"
    private var ftpClient: FTPClient? = null
    private var isFtpClientOpened = false

    init {
        /*String server, String user, String password
        this.server = server;
        this.user = user;
        this.password = password;*/
    }

    // region Protected Methods
    protected fun openFTPConnection(): Result {
        val result = Result()
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

        //TODO: Generic Exception handler

        return result
    }

    fun testFTPConnection() : Result {

        val openConnectionResult = openFTPConnection()
        closeFtpConnection()

        return openConnectionResult

    }

}
