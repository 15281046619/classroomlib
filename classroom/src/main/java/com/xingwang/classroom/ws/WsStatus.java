package com.xingwang.classroom.ws;

class WsStatus {



    final static int CONNECTED = 1;

    final static int CONNECTING = 0;

    final static int RECONNECT = 2;

    final static int DISCONNECTED = -1;
    class CODE {

        final static int NORMAL_CLOSE = 1000;
        final static int ABNORMAL_CLOSE = 1001;

    }


    class TIP {
        final static String NORMAL_CLOSE = "normal close";

        final static String ABNORMAL_CLOSE = "abnormal close";

    }

}
