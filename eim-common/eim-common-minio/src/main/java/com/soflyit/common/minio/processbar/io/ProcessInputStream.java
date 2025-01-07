package com.soflyit.common.minio.processbar.io;

import me.tongfei.progressbar.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

/**
 * 带进度处理的输入流<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-23 17:04
 */
public class ProcessInputStream extends InputStream {
    private final InputStream in;
    private final ProgressBar progressBar;

    public ProcessInputStream(InputStream stream, ProgressBar progressBar) throws IOException {
        super();


        this.in = stream;


        this.progressBar = progressBar;
    }

    @Override
    public int available() throws IOException {
        return this.in.available();
    }

    @Override
    public void close() throws IOException {
        this.in.close();
        return;
    }

    @Override
    public int read() throws IOException {
        if (this.progressBar != null) {
            this.progressBar.step();
        }
        return this.in.read();
    }

    @Override
    public int read(byte[] toStore) throws IOException {
        int readBytes = this.in.read(toStore);
        if (this.progressBar != null) {
            this.progressBar.stepBy(readBytes); // Update progress bar.
        }
        return readBytes;
    }

    @Override
    public int read(byte[] toStore, int off, int len) throws IOException {
        int readBytes = this.in.read(toStore, off, len);
        if (this.progressBar != null) {
            this.progressBar.stepBy(readBytes);
        }
        return readBytes;
    }

    @Override
    public long skip(long n) throws IOException {
        if (this.progressBar != null) {
            this.progressBar.stepTo(n);
        }
        return this.in.skip(n);
    }

}
