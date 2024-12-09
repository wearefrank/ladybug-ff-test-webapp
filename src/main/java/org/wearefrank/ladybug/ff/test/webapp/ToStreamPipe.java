package org.wearefrank.ladybug.ff.test.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.frankframework.core.PipeLineSession;
import org.frankframework.core.PipeRunException;
import org.frankframework.core.PipeRunResult;
import org.frankframework.pipes.FixedForwardPipe;
import org.frankframework.stream.Message;

public class ToStreamPipe extends FixedForwardPipe {
    public static enum StreamKind {
        BINARY,
        CHARACTER
    }

    private StreamKind streamKind = StreamKind.BINARY;

    public void setStreamKind(StreamKind streamKind) {
        this.streamKind = streamKind;
    }

    public PipeRunResult doPipe(Message message, PipeLineSession session) throws PipeRunException {
        try {
            switch(streamKind) {
                case CHARACTER: {
                        Reader r = message.asReader();
                        Message out = new Message(r);
                        return new PipeRunResult(getSuccessForward(), out);
                    }
                case BINARY: {
                        InputStream is = message.asInputStream();
                        Message out = new Message(is);
                        return new PipeRunResult(getSuccessForward(), is);
                    }
            }
        }
        catch(IOException e) {
            throw new PipeRunException(this, "IOException", e);
        }
        throw new PipeRunException(this, "Code should be unreachable because all branches of switch return");
    }
}