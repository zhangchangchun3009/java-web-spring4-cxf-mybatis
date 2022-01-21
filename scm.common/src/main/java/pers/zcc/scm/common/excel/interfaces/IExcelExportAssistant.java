
package pers.zcc.scm.common.excel.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.zcc.scm.common.excel.vo.ExcelExportVO;

/**
 * The Interface IExcelExportAssistant.
 *
 * @author zhangchangchun
 * @since 2021年4月12日
 */
public interface IExcelExportAssistant {

    /**
     * 根据查询条件从数据库获取数据并导出为excel文件
     * //文件输出方式暂定为同步流式响应，后续可改造为异步处理
     *
     * @param request http请求
     * @param response the http响应
     * @param excelExportVO 导出配置，包括对象映射关系和数据库数据源查询配置
     * @return 返回文件名，依文件名调下载接口下载文件
     * @throws Exception 各种异常
     */
    String export(HttpServletRequest request, HttpServletResponse response, final ExcelExportVO excelExportVO)
            throws Exception;

    /**
     * 自定义数据源导出为excel
     * 导出文件输出到D:\excel\export目录
     * 只支持同步处理。由于是内存数据源所以性能应该比较快，问题是内存数据占用内存，所以不适合大量数据
     * 成功时无返回，错误时抛出异常
     *
     * @param <T> the generic type 数据源的数据java类型
     * @param provider the provider 数据提供者，返回有序集合，不支持分批数据源
     * @param excelExportVO 导出配置，不需要配置数据库数据源查询相关
     * @return 导出成功时返回文件名，依文件名调下载接口下载文件
     * @throws Exception the exception 各种异常
     */
    <T> String export(IExcelExportDataProvider<T> provider, final ExcelExportVO excelExportVO) throws Exception;
}
