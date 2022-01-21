package pers.zcc.scm.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import pers.zcc.scm.common.vo.ExcelErrorVO;

public interface IExcelErrorDao {

    List<ExcelErrorVO> getList(@Param("entity") ExcelErrorVO param);

    void insert(@Param("list") List<ExcelErrorVO> itemsToCreate);

    void update(@Param("list") List<ExcelErrorVO> itemsToUpdate);

    void delete(@Param("list") List<ExcelErrorVO> itemsToDelete);

}
