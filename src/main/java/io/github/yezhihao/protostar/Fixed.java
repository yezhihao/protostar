package io.github.yezhihao.protostar;

public interface Fixed {

    interface L8<T> extends Schema<T>, Fixed {
        default int length() {
            return 8;
        }
    }

    interface L6<T> extends Schema<T>, Fixed {
        default int length() {
            return 6;
        }
    }

    interface L4<T> extends Schema<T>, Fixed {
        default int length() {
            return 4;
        }
    }

    interface L3<T> extends Schema<T>, Fixed {
        default int length() {
            return 3;
        }
    }

    interface L2<T> extends Schema<T>, Fixed {
        default int length() {
            return 2;
        }
    }

    interface L1<T> extends Schema<T>, Fixed {
        default int length() {
            return 1;
        }
    }
}