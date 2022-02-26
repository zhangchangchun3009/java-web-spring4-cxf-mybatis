package pers.zcc.scm.common.excel.interfaces;

import java.io.InputStream;

import pers.zcc.scm.common.excel.vo.ExcelImportVO;

public interface IExcelImportAssistant {

    void importExcel(InputStream fin, ExcelImportVO importContext);

}
