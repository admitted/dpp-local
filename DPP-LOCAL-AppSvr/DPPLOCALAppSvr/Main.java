package DPPLOCALAppSvr;

import container.ActionContainer;
import net.MsgCtrl;
import net.TcpSvr;
import org.apache.log4j.PropertyConfigurator;
import util.DBUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Main 是一个线程
 * 自己有一个 main 方法
 *
 * 又有一个 run 方法
 *
 * @author cui
 * @date 2017/5/5
 */
public class Main extends Thread {
    private static Main objMain = null;

    private DBUtil m_DBUtil = null;              //数据库
    private TcpSvr m_TcpSvr = null;              //Svr
    private MsgCtrl m_MsgCtrl = null;            //Svr消息控制

    /**
     * 由 $JAVA_HOME/bin/java DPPLOCALAppSvr.Main 执行此 main 方法
     * @param args
     */
    public static void main(String[] args) {
        objMain = new Main();
        objMain.init();
    }

    /**
     * Main 类初始化方法
     */
    public void init() {
        try {
            PropertyConfigurator.configure("log4j.properties");  //加载properties文件

            //指令回复容器 (没初始化成功：异常退出)
            if (!ActionContainer.Initialize()) {
                System.exit(-1);  // System.exit(0)是正常退出程序，非0表示非正常退出程序
            }

            //数据库初始化
            m_DBUtil = new DBUtil();
            if (!m_DBUtil.init()) {
                System.exit(-1);
            }

            //TCPSVR初始化
            m_TcpSvr = new TcpSvr(m_DBUtil);
            if (!m_TcpSvr.init()) {
                System.out.println("m_TcpSvr Failed======");
                System.exit(-3);
            }

            //消息控制初始化
            m_MsgCtrl = new MsgCtrl(m_TcpSvr, m_DBUtil);
            if (!m_MsgCtrl.Initialize()) {
                System.out.println("m_MsgCtrl Failed======");
                System.exit(0);
            }

            this.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.gc();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(0);
        }

    }

    public void run() {
        System.out.println("Start..........................................");
        while (!interrupted()) {
            try {
                sleep(1000);
                if (true)
                    continue;
                @SuppressWarnings("unused")
                String inputCmd = new BufferedReader(new InputStreamReader(System.in)).readLine().toLowerCase();
                System.out.println();

            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }
}
