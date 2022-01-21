
package pers.zcc.scm.common.frame;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import pers.zcc.scm.common.util.ApplicationContextManager;

/**
 * The listener interface for receiving slfContextLoader events. The class that
 * is interested in processing a slfContextLoader event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's <code>addSlfContextLoaderListener<code>
 * method. When the slfContextLoader event occurs, that object's appropriate
 * method is invoked.
 *
 * @author zhangchangchun
 * @since 2021年4月21日
 */
public class SlfContextLoaderListener extends ContextLoaderListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlfContextLoaderListener.class);

    public SlfContextLoaderListener() {
        super();
    }

    public SlfContextLoaderListener(WebApplicationContext context) {
        super(context);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOGGER.info("欢迎使用zcc封装的简易轻量spring框架，应用启动中，请稍后。。。");
        LOGGER.info("*   ............HIIMHIMHMMHMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM:.............");
        LOGGER.info("*   ...........MMMI:MII:MIHMHMMMMMHMMMMIMMMIMMMMMMMMMMMMMM.............");
        LOGGER.info("*   .........:MMMI:M::HM::MIHHHM:IM:MHM:IMH:IMMMIIMMMHMMMH:............");
        LOGGER.info("*   ........:MMMHHM::MMI:HH:MM:I:M:MMMH:IMH:IMM::MH:MM:MM:M............");
        LOGGER.info("*   .......MMMMHMM:MMIMHMII:MM:IIIM:MHMIMMM:MMIIH::MH:HM:M............。");
        LOGGER.info("*   ...... :MIMMMMMMMMMHMMHMM:HHMHMMMMIMHMMMMHMHMIHIHM::MMI............");
        LOGGER.info("*   .......M:MMMMMMMMMMMMHMMMMMMMMHMMMMMMMMMMHMMMHMMIMHMMMM:...........");
        LOGGER.info("*   .......HHMMMMMMMMMMMMMMMMIMMMM.MMHMMMMHMMMMMMMMMMMIMHMMI...........");
        LOGGER.info("*   ........MMMIMMMMMMMMMMIHMIMIM:.M:HMM:MIHMMMMMMMMMMMMMMMI...........");
        LOGGER.info("*   ........MM.MMMMMMMMMMMH:MMMHM:.M.:MM.M.HMMMHMMMMMMMMMMMI...........");
        LOGGER.info("*   ........MM.MMMMMMMMMMH::M.M.M..M..MM:M.IIMH:MMMMMMMMMMMI...........");
        LOGGER.info("*   ........M:.MM:MMMMMMM:.I..:.I..H..IM:I.I.M.IMHMMMMMMMMMI...........");
        LOGGER.info("*   ........M..MMMHMMMMM.:HI:HHH......H....II..:MMMMMMMHMM:...........。");
        LOGGER.info("*   ...........MM.MIMMMM.:H: .::I........ ...:IH:.MMMMMM:MMI...........");
        LOGGER.info("*   ...........:H..MMMIM.M.:...:I.........: ..::HHHMMMMIMMMH...........");
        LOGGER.info("*   ............H..IMM:I:..I:..:..........I:..:I:::MMMMMMMMM...........");
        LOGGER.info("*   ...............MMH::I...I:::..........III.I::MMMHMMMMMMM...........");
        LOGGER.info("*   ..............MMMHMII.. ..............:MI:I.HIIIHM:HMMMM...........");
        LOGGER.info("*   .............:MMMMIM::...........:......:H..:II:MHIMMMMM...........");
        LOGGER.info("*   .............MMMMMMMMM......................I.MMMHHMMMMM:..........");
        LOGGER.info("*   ............MMMMH:MMMMM........:.:.........:MMMMMMMMMMMMI..........");
        LOGGER.info("*   ...........MMMMMMHMMMMMMM.. .............:MMMMMMMMMMMMMMM..........");
        LOGGER.info("*   ........ .IMMMMMMMMMMMMMMMM............:MMMMMMMMMHMMMMMMM..........");
        LOGGER.info("*   ..........MMMMMMMMMMMMMMMMMI:.......:IHMMMMMMMMM::MMMMMMM..........");
        LOGGER.info("*   .........MMMMMMMIMMMMMMMMMMH:::I:I:.::HMMMMMMMMMHMMMMMMMMI.........");
        LOGGER.info("*   ........HMMMMMMM:MMMMMMMMMMI:::::::..:HMMMMMMMMIMMMMMMMMMM.........");
        LOGGER.info("*   .......IMMMMM..MI:MMMMMMMMM::::..:::::.MMMMMMMMMMMMMMMMMMM ........");
        LOGGER.info("*   ......:MMMM....:MMHMMMMMM:::::.....::.:.MMMMMMMHMMMMMMMMMM.........");
        LOGGER.info("*   ......MMI....:::IMMMMI:.:::::.....:I::::::HMMMHIM::::::MMMH........");
        LOGGER.info("*   .....MM.....:::IMIMMM:.......:....::::::::IMMHMM:I:::I:::MM .......");
        LOGGER.info("*   ....MMM.....:I:.MIHMMM........:...:::::::HMMMMI:::::....::M:.......");
        LOGGER.info("*   ...:MM:........:MMMHMM..................IMMMMM::...........M.......");
        LOGGER.info("*   ...MMM........:::MMMMM......:..........:HMMMMM::...................");
        LOGGER.info("*   ..:MM:.......:::::MMMM........... .. .::MMM:HM::.............:.....");
        LOGGER.info("*   ..MMM........:::::MMMMM:I..HHIMHMHHHM :IMMHIMH::.............I....。");
        LOGGER.info("*   ============================================================================= ");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!`   `4!!!!!!!!!!~4!!!!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   <~:   ~!!!~   ..  4!!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  ~~~~~~~  '  ud$$$$$  !!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  ~~~~~~~~~: ?$$$$$$$$$  !!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!`     ``~!!!!!!!!!!!!!!  ~~~~~          \"*$$$$$k `!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!  $$$$$bu.  '~!~`     .  '~~~~      :~~~~          `4!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!  $$$$$$$$$$$c  .zW$$$$$E ~~~~      ~~~~~~~~  ~~~~~:  '!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!! d$$$$$$$$$$$$$$$$$$$$$$E ~~~~~    '~~~~~~~~    ~~~~~  !!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!> 9$$$$$$$$$$$$$$$$$$$$$$$ '~~~~~~~ '~~~~~~~~     ~~~~  !!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!> $$$$$$$$$$$$$$$$$$$$$$$$b   ~~~    '~~~~~~~     '~~~ '!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!> $$$$$$$$$$$$$$$$$$$$$$$$$$$cuuue$$N.   ~        ~~~  !!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!! **$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Ne  ~~~~~~~~  `!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!  J$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$N  ~~~~~  zL '!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!  d$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$c     z$$$c `!!!!!!!!!");
        LOGGER.info("*   !!!!!!!> <$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$> 4!!!!!!!!");
        LOGGER.info("*   !!!!!!!  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  !!!!!!!!");
        LOGGER.info("*   !!!!!!! <$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*\"   ....:!!");
        LOGGER.info("*   !!!!!!~ 9$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$e@$N '!!!!!!!");
        LOGGER.info("*   !!!!!!  9$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  !!!!!!!");
        LOGGER.info("*   !!!!!!  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\"\"$$$$$$$$$$$~ ~~4!!!!");
        LOGGER.info("*   !!!!!!  9$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$    $$$$$$$Lue  :::!!!!");
        LOGGER.info("*   !!!!!!> 9$$$$$$$$$$$$\" '$$$$$$$$$$$$$$$$$$$$$$$$$$$    $$$$$$$$$$  !!!!!!!");
        LOGGER.info("*   !!!!!!! '$$*$$$$$$$$E   '$$$$$$$$$$$$$$$$$$$$$$$$$$$u.@$$$$$$$$$E '!!!!!!!");
        LOGGER.info("*   !!!!~`   .eeW$$$$$$$$   :$$$$$$$$$$$$$***$$$$$$$$$$$$$$$$$$$$u.    `~!!!!!");
        LOGGER.info("*   !!> .:!h '$$$$$$$$$$$$ed$$$$$$$$$$$$Fz$$b $$$$$$$$$$$$$$$$$$$$$F '!h.  !!!");
        LOGGER.info("*   !!!!!!!!L '$**$$$$$$$$$$$$$$$$$$$$$$ *$$$ $$$$$$$$$$$$$$$$$$$$F  !!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!   d$$$$$$$$$$$$$$$$$$$$$$$$buud$$$$$$$$$$$$$$$$$$$$\"  !!!!!!!!!!");
        LOGGER.info("*   !!!!!!! .<!  #$$*\"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*  :!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!:   d$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$#  :!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!~  :  '#$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*\"    !!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!  !!!!!:   ^\"**$$$$$$$$$$$$$$$$$$$$**#\"     .:<!!!!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!:...                      .::!!!!!!!!!!!!!!!!!!!!!!!!");
        LOGGER.info("*   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        LOGGER.info("*   ============================================================================== ");
        LOGGER.info("      nHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHn.");
        LOGGER.info("   .MS?MMMMMMMMMMMMMMMMMM?MM~MMMMMMMMMSHMMMMMMMM(?\"~\\");
        LOGGER.info("   MMMMMH?MMMMMMMX*MM?MMX%MM/MMMMMM\"HMMMMMMMMMMMMMMH");
        LOGGER.info("  MMMMMMMMMMMMMMMMMX*MX*MMMX?MMMMM(M!XMMMMMMMMMMMMMMMX");
        LOGGER.info(" XMC)?MMMMMMMMMMMMMMMhX?!?MMMMX#MM!MXMMMMMMMMMMMML   '~");
        LOGGER.info("'\\      MMMMMMMMMMMMMMMMMMMMMMMM!~`````-`~!?MMMM)MMMMMMMMx");
        LOGGER.info("`~\"\"MMM)MMMMMMMMMMMMMMMHhHH!~           `#MM(MMMMMMMMMM>");
        LOGGER.info("HM!HMMMMMMMMMMMMMMMM*?)?`                `\"MMMMMMMMMX       .");
        LOGGER.info("XM!MMMMMMMMMMMMMMMMMMM?~                     'MMMMMMMM:..xx!`");
        LOGGER.info("M!MMMMMMMMMMMMMMMMMXH!                        MMMMXMMP\"`");
        LOGGER.info("\\!MMMMMMMSMHHHMM?XMM?~    -:::xx..             M?XMM?\".x(");
        LOGGER.info("MXMMMMMMMMMM!XHMMMM\":       ... `\"%x          XHHHMMM*\"");
        LOGGER.info("\\!MMMMMMMM?XMMMMMMX!'~L     '%%%+:.  `       ..MMMMM\"");
        LOGGER.info("'HMMMMMM?HMMMMM*XM!    h     ~\\).^\\~     .%\"\"`MM?\"");
        LOGGER.info("'MMMMMMMMMMMMMXMMM!    -X               +%%!.MMMXk");
        LOGGER.info("?MMMMMMMMMMMXMMMMM `.   ~               `\"\"'XMMMMX");
        LOGGER.info("!MMMMMMMMMMMMMMMMMX.    '                  XMkMMX>");
        LOGGER.info("XMMMMMMMMMMMMMMM?MXXXx.-`                  XXMMM!");
        LOGGER.info("MMMMMMMMMMMMMMMMXMXXXXXXx.         ~~      MMMMM");
        LOGGER.info("XMMMMMMMMMMMM?MMXXXXXXXXX!`         '+^  .MMM!P");
        LOGGER.info("'MMM!MMMMMMMMMi?M!\"`        `~%HHHHxx.  xMMMM\"");
        LOGGER.info(":MMMMMMMMMMMMMMM\"               `\\XMM .MMMMM");
        LOGGER.info("XMMMMMMMMMX?MM!                   `( HMMMMM");
        LOGGER.info("XMMMM)MMM\"   \\~                     'MMMMM*");
        LOGGER.info("'MMMMfMMM\"  \\~                        XMMM*");
        LOGGER.info(".MMMMMXMM\"  ^                          `MMM");
        LOGGER.info("XMMMM!MM\"                               MM>");
        LOGGER.info("HMMMMXM~                                MM>");
        LOGGER.info("?MMMMM~                                 Xf%");
        LOGGER.info("MMMMf                                  %% \\");
        LOGGER.info("4MMM                                    %");
        LOGGER.info("`M                                     %");
        LOGGER.info("%                                    %");
        LOGGER.info("%                                     %");
        LOGGER.info("%                 !                   %");
        LOGGER.info("%                 !                   %");
        LOGGER.info("%                 %                   !?%.");
        LOGGER.info("%                  %                   X. %%.");
        LOGGER.info("%                  %                  X!    %%.");
        LOGGER.info("%                  %                  '!       %.");
        LOGGER.info(" %                  %                  !!         %.");
        LOGGER.info(" %                  %                  '!          `%");
        LOGGER.info("  %                  %                  !>          /%");
        LOGGER.info("  %                   %                 !!          % %");
        LOGGER.info("   %                   %                 !          \\%");
        LOGGER.info("   %                   %                 !!          %");
        LOGGER.info("    %                   %                '!         %~");
        LOGGER.info("     %                   %                !!       %~");
        LOGGER.info("     %%                  %                `!     %%");
        LOGGER.info("     %%%                  %                %++4MMf");
        LOGGER.info("      ?MMx                 %                %. MMX");
        LOGGER.info("       *MMMx               %                 !\'MMM>");
        LOGGER.info("        MMMMMHx    .....xxnH                  %HMMM>");
        LOGGER.info("         MMMMMMMMMMMMMMMMMMM>                  MMMMX");
        LOGGER.info("         'MMMMMMMMMMMMMMMMMMk                  'MMMM");
        LOGGER.info("          'MMMMMMMMMMMMMMMMMM                   MMMM>");
        LOGGER.info("           ?MMMMMMMMMMMMMMMMMM                  'MMMX");
        LOGGER.info("            MMMMMMMMMMMMMMMMMMM                  MMMM");
        LOGGER.info("            XMMMMM");

        super.contextInitialized(event);
        try {
            WebApplicationContext context = ApplicationContextManager.getApplicationContext();
            Map<String, IAfterStartUpHandle> beanMap = context.getBeansOfType(IAfterStartUpHandle.class);
            if (beanMap == null || beanMap.isEmpty()) {
                return;
            }
            Collection<IAfterStartUpHandle> beans = beanMap.values();
            beans.stream().sorted((o1, o2) -> {
                return o1.getOrder() - o2.getOrder();
            }).forEach(item -> {
                item.process();
            });
        } catch (BeansException e) {
            LOGGER.error("contextInitialized e,", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            WebApplicationContext context = ApplicationContextManager.getApplicationContext();
            Map<String, IBeforeShutDownHandle> beanMap = context.getBeansOfType(IBeforeShutDownHandle.class);
            if (beanMap != null && !beanMap.isEmpty()) {
                Collection<IBeforeShutDownHandle> beans = beanMap.values();
                beans.stream().sorted((o1, o2) -> {
                    return o1.getOrder() - o2.getOrder();
                }).forEach(item -> {
                    item.process();
                });
            }
        } catch (BeansException e) {
            LOGGER.error("contextDestroyed e,", e);
        }
        super.contextDestroyed(event);
    }

}
