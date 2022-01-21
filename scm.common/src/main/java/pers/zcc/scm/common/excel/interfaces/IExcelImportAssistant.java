package pers.zcc.scm.common.excel.interfaces;

import java.io.FileInputStream;

import pers.zcc.scm.common.excel.vo.ExcelImportVO;

public interface IExcelImportAssistant {

    void importExcel(FileInputStream fin, ExcelImportVO importContext);

}
