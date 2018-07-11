package com.example.shinya.motion_recognition

import java.io.*


class CsvReader {

    fun parse(file: File): MotionData {
        var data = MotionData()

        try {
            // CSVファイルの読み込み
            val bufferReader = BufferedReader(FileReader(file))
            var line: String?

            do {

                line = bufferReader.readLine()

                if (line == null) break

                val rowData = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                //CSVの左([0]番目)から順番にセット
                data.x.add(rowData[0].toFloat())
                data.y.add(rowData[1].toFloat())
                data.z.add(rowData[2].toFloat())
            } while (true)
            bufferReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return data
    }
}