package com.zxs.common.utils;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.CellValue;

/**
 * 通过字符串计算最终的结果值
 *
 * @author zhangPengyi
 * @date 2019/12/10
 */
public class CalculatorUtils {

    /**
     * 通过字符串计算最终的结果值（JAVA自带）
     *
     * @author zhangPengyi
     * @date 2019/12/10
     */
    public static Double formulaToResult(String str) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        BigDecimal result = new BigDecimal(engine.eval(str).toString());
        return result.doubleValue();
    }

    /**
     * 通过字符串计算最终的结果值（通过Excel计算，可计算sum,if,and,or等Excel支持公式），推荐使用
     * A>B && A>C 拼接为"AND(10>20,10>8)",返回false
     *
     * @author zhangPengyi
     * @date 2019/12/10
     */
    public static CellValue conversionExcelFormula(HSSFCell cell, String formula) {
        cell.setCellFormula(formula);
        HSSFFormulaEvaluator formulaEvaluator =
                cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        CellValue cellVal = formulaEvaluator.evaluate(cell);
        return cellVal;
    }

}
