package com.eyunda.third.adapters.chat.widget;

import android.text.TextUtils;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

public class HanziToPinyin
{
  private static final String TAG = "HanziToPinyin";
  public static final char[] UNIHANS = { 38463, '哎', '安', 32942, '凹', '八', '挀', '扳', 37030, '勹', 38466, '奔', '伻', '屄', 36793, '灬', '憋', '汃', '冫', '癶', '峬', '嚓', '偲', '参', '仓', '撡', '冊', '嵾', '曽', '曾', '層', '叉', 33414, 36799, '伥', '抄', 36710, '抻', '沈', '沉', 38455, '吃', '充', '抽', '出', '欻', '揣', '巛', '刅', '吹', '旾', 36916, '呲', '匆', '凑', '粗', '汆', '崔', 37032, '搓', '咑', '呆', '丹', '当', '刀', '嘚', '扥', '灯', '氐', '嗲', '甸', '刁', '爹', '丁', '丟', '东', '吺', '厾', 32785, 35176, '吨', '多', '妸', 35830, '奀', 38821, '儿', '发', '帆', '匚', 39134, '分', '丰', 35205, '仏', '紑', '伕', '旮', '侅', '甘', '冈', '皋', '戈', '给', '根', '刯', '工', '勾', '估', '瓜', '乖', '关', '光', '归', '丨', '呙', '哈', '咍', '佄', '夯', 33568, 35779, 40658, '拫', '亨', '噷', '叿', 40769, '乯', 33457, '怀', '犿', '巟', '灰', '昏', '吙', '丌', '加', '戋', '江', 33405, 38454, '巾', '坕', '冂', '丩', '凥', '姢', '噘', '军', '咔', '开', '刊', '忼', '尻', '匼', 32910, '劥', '空', '抠', '扝', '夸', 33967, '宽', '匡', '亏', '坤', '扩', '垃', '来', '兰', '啷', '捞', 32907, '勒', '崚', '刕', '俩', '奁', 33391, '撩', '列', '拎', '刢', '溜', '囖', 40857, '瞜', '噜', '娈', '畧', '抡', '罗', '呣', '妈', '埋', '嫚', '牤', '猫', '么', '呅', 38376, '甿', '咪', '宀', '喵', '乜', '民', '名', 35884, '摸', '哞', '毪', '嗯', '拏', 33097, '囡', '囔', '孬', '疒', '娞', '恁', 33021, '妮', '拈', '嬢', 40479, '捏', '囜', '宁', '妞', '农', '羺', '奴', '奻', '疟', 40641, 37069, '喔', 35764, '妑', '拍', '眅', '乓', '抛', '呸', '喷', '匉', '丕', '囨', '剽', '氕', '姘', '乒', 38027, '剖', '仆', '七', '掐', '千', '呛', '悄', '癿', '亲', '狅', 33422, '丘', '区', '峑', '缺', '夋', '呥', '穣', '娆', '惹', '人', '扔', '日', 33592, '厹', 37018, '挼', '堧', '婑', '瞤', '捼', '仨', '毢', '三', '桒', '掻', 38314, '森', '僧', '杀', '筛', '山', '伤', '弰', '奢', '申', 33688, '敒', '升', '尸', '収', '书', '刷', 34928, 38377, '双', 35841, '吮', 35828, '厶', '忪', '捜', 33487, '狻', '夊', '孙', '唆', '他', '囼', '坍', '汤', '夲', '忑', '熥', '剔', '天', '旫', '帖', '厅', '囲', '偷', '凸', '湍', '推', '吞', '乇', '穵', '歪', '弯', '尣', '危', '昷', '翁', '挝', '乌', '夕', 34418, '仚', '乡', '灱', '些', '心', '星', '凶', '休', '吁', '吅', '削', '坃', '丫', '恹', '央', '幺', '倻', '一', '囙', '应', '哟', '佣', '优', '扜', '囦', '曰', '晕', '筠', '筼', '帀', '災', '兂', '匨', '傮', '则', 36156, '怎', '増', '扎', '捚', '沾', '张', 38271, 38263, '佋', 34567, 36126, '争', '之', '峙', '庢', '中', '州', '朱', '抓', '拽', '专', '妆', 38585, '宒', '卓', '乲', '宗', 37049, '租', 38075, '厜', '尊', '昨', '兙', 40899, 40900 };
  public static final byte[][] PINYINS = { { 65, 0, 0, 0, 0, 0 }, { 65, 73, 0, 0, 0, 0 }, { 65, 78, 0, 0, 0, 0 }, { 65, 78, 71, 0, 0, 0 }, { 65, 79, 0, 0, 0, 0 }, { 66, 65, 0, 0, 0, 0 }, { 66, 65, 73, 0, 0, 0 }, { 66, 65, 78, 0, 0, 0 }, { 66, 65, 78, 71, 0, 0 }, { 66, 65, 79, 0, 0, 0 }, { 66, 69, 73, 0, 0, 0 }, { 66, 69, 78, 0, 0, 0 }, { 66, 69, 78, 71, 0, 0 }, { 66, 73, 0, 0, 0, 0 }, { 66, 73, 65, 78, 0, 0 }, { 66, 73, 65, 79, 0, 0 }, { 66, 73, 69, 0, 0, 0 }, { 66, 73, 78, 0, 0, 0 }, { 66, 73, 78, 71, 0, 0 }, { 66, 79, 0, 0, 0, 0 }, { 66, 85, 0, 0, 0, 0 }, { 67, 65, 0, 0, 0, 0 }, { 67, 65, 73, 0, 0, 0 }, { 67, 65, 78, 0, 0, 0 }, { 67, 65, 78, 71, 0, 0 }, { 67, 65, 79, 0, 0, 0 }, { 67, 69, 0, 0, 0, 0 }, { 67, 69, 78, 0, 0, 0 }, { 67, 69, 78, 71, 0, 0 }, { 90, 69, 78, 71, 0, 0 }, { 67, 69, 78, 71, 0, 0 }, { 67, 72, 65, 0, 0, 0 }, { 67, 72, 65, 73, 0, 0 }, { 67, 72, 65, 78, 0, 0 }, { 67, 72, 65, 78, 71, 0 }, { 67, 72, 65, 79, 0, 0 }, { 67, 72, 69, 0, 0, 0 }, { 67, 72, 69, 78, 0, 0 }, { 83, 72, 69, 78, 0, 0 }, { 67, 72, 69, 78, 0, 0 }, { 67, 72, 69, 78, 71, 0 }, { 67, 72, 73, 0, 0, 0 }, { 67, 72, 79, 78, 71, 0 }, { 67, 72, 79, 85, 0, 0 }, { 67, 72, 85, 0, 0, 0 }, { 67, 72, 85, 65, 0, 0 }, { 67, 72, 85, 65, 73, 0 }, { 67, 72, 85, 65, 78, 0 }, { 67, 72, 85, 65, 78, 71 }, { 67, 72, 85, 73, 0, 0 }, { 67, 72, 85, 78, 0, 0 }, { 67, 72, 85, 79, 0, 0 }, { 67, 73, 0, 0, 0, 0 }, { 67, 79, 78, 71, 0, 0 }, { 67, 79, 85, 0, 0, 0 }, { 67, 85, 0, 0, 0, 0 }, { 67, 85, 65, 78, 0, 0 }, { 67, 85, 73, 0, 0, 0 }, { 67, 85, 78, 0, 0, 0 }, { 67, 85, 79, 0, 0, 0 }, { 68, 65, 0, 0, 0, 0 }, { 68, 65, 73, 0, 0, 0 }, { 68, 65, 78, 0, 0, 0 }, { 68, 65, 78, 71, 0, 0 }, { 68, 65, 79, 0, 0, 0 }, { 68, 69, 0, 0, 0, 0 }, { 68, 69, 78, 0, 0, 0 }, { 68, 69, 78, 71, 0, 0 }, { 68, 73, 0, 0, 0, 0 }, { 68, 73, 65, 0, 0, 0 }, { 68, 73, 65, 78, 0, 0 }, { 68, 73, 65, 79, 0, 0 }, { 68, 73, 69, 0, 0, 0 }, { 68, 73, 78, 71, 0, 0 }, { 68, 73, 85, 0, 0, 0 }, { 68, 79, 78, 71, 0, 0 }, { 68, 79, 85, 0, 0, 0 }, { 68, 85, 0, 0, 0, 0 }, { 68, 85, 65, 78, 0, 0 }, { 68, 85, 73, 0, 0, 0 }, { 68, 85, 78, 0, 0, 0 }, { 68, 85, 79, 0, 0, 0 }, { 69, 0, 0, 0, 0, 0 }, { 69, 73, 0, 0, 0, 0 }, { 69, 78, 0, 0, 0, 0 }, { 69, 78, 71, 0, 0, 0 }, { 69, 82, 0, 0, 0, 0 }, { 70, 65, 0, 0, 0, 0 }, { 70, 65, 78, 0, 0, 0 }, { 70, 65, 78, 71, 0, 0 }, { 70, 69, 73, 0, 0, 0 }, { 70, 69, 78, 0, 0, 0 }, { 70, 69, 78, 71, 0, 0 }, { 70, 73, 65, 79, 0, 0 }, { 70, 79, 0, 0, 0, 0 }, { 70, 79, 85, 0, 0, 0 }, { 70, 85, 0, 0, 0, 0 }, { 71, 65, 0, 0, 0, 0 }, { 71, 65, 73, 0, 0, 0 }, { 71, 65, 78, 0, 0, 0 }, { 71, 65, 78, 71, 0, 0 }, { 71, 65, 79, 0, 0, 0 }, { 71, 69, 0, 0, 0, 0 }, { 71, 69, 73, 0, 0, 0 }, { 71, 69, 78, 0, 0, 0 }, { 71, 69, 78, 71, 0, 0 }, { 71, 79, 78, 71, 0, 0 }, { 71, 79, 85, 0, 0, 0 }, { 71, 85, 0, 0, 0, 0 }, { 71, 85, 65, 0, 0, 0 }, { 71, 85, 65, 73, 0, 0 }, { 71, 85, 65, 78, 0, 0 }, { 71, 85, 65, 78, 71, 0 }, { 71, 85, 73, 0, 0, 0 }, { 71, 85, 78, 0, 0, 0 }, { 71, 85, 79, 0, 0, 0 }, { 72, 65, 0, 0, 0, 0 }, { 72, 65, 73, 0, 0, 0 }, { 72, 65, 78, 0, 0, 0 }, { 72, 65, 78, 71, 0, 0 }, { 72, 65, 79, 0, 0, 0 }, { 72, 69, 0, 0, 0, 0 }, { 72, 69, 73, 0, 0, 0 }, { 72, 69, 78, 0, 0, 0 }, { 72, 69, 78, 71, 0, 0 }, { 72, 77, 0, 0, 0, 0 }, { 72, 79, 78, 71, 0, 0 }, { 72, 79, 85, 0, 0, 0 }, { 72, 85, 0, 0, 0, 0 }, { 72, 85, 65, 0, 0, 0 }, { 72, 85, 65, 73, 0, 0 }, { 72, 85, 65, 78, 0, 0 }, { 72, 85, 65, 78, 71, 0 }, { 72, 85, 73, 0, 0, 0 }, { 72, 85, 78, 0, 0, 0 }, { 72, 85, 79, 0, 0, 0 }, { 74, 73, 0, 0, 0, 0 }, { 74, 73, 65, 0, 0, 0 }, { 74, 73, 65, 78, 0, 0 }, { 74, 73, 65, 78, 71, 0 }, { 74, 73, 65, 79, 0, 0 }, { 74, 73, 69, 0, 0, 0 }, { 74, 73, 78, 0, 0, 0 }, { 74, 73, 78, 71, 0, 0 }, { 74, 73, 79, 78, 71, 0 }, { 74, 73, 85, 0, 0, 0 }, { 74, 85, 0, 0, 0, 0 }, { 74, 85, 65, 78, 0, 0 }, { 74, 85, 69, 0, 0, 0 }, { 74, 85, 78, 0, 0, 0 }, { 75, 65, 0, 0, 0, 0 }, { 75, 65, 73, 0, 0, 0 }, { 75, 65, 78, 0, 0, 0 }, { 75, 65, 78, 71, 0, 0 }, { 75, 65, 79, 0, 0, 0 }, { 75, 69, 0, 0, 0, 0 }, { 75, 69, 78, 0, 0, 0 }, { 75, 69, 78, 71, 0, 0 }, { 75, 79, 78, 71, 0, 0 }, { 75, 79, 85, 0, 0, 0 }, { 75, 85, 0, 0, 0, 0 }, { 75, 85, 65, 0, 0, 0 }, { 75, 85, 65, 73, 0, 0 }, { 75, 85, 65, 78, 0, 0 }, { 75, 85, 65, 78, 71, 0 }, { 75, 85, 73, 0, 0, 0 }, { 75, 85, 78, 0, 0, 0 }, { 75, 85, 79, 0, 0, 0 }, { 76, 65, 0, 0, 0, 0 }, { 76, 65, 73, 0, 0, 0 }, { 76, 65, 78, 0, 0, 0 }, { 76, 65, 78, 71, 0, 0 }, { 76, 65, 79, 0, 0, 0 }, { 76, 69, 0, 0, 0, 0 }, { 76, 69, 73, 0, 0, 0 }, { 76, 69, 78, 71, 0, 0 }, { 76, 73, 0, 0, 0, 0 }, { 76, 73, 65, 0, 0, 0 }, { 76, 73, 65, 78, 0, 0 }, { 76, 73, 65, 78, 71, 0 }, { 76, 73, 65, 79, 0, 0 }, { 76, 73, 69, 0, 0, 0 }, { 76, 73, 78, 0, 0, 0 }, { 76, 73, 78, 71, 0, 0 }, { 76, 73, 85, 0, 0, 0 }, { 76, 79, 0, 0, 0, 0 }, { 76, 79, 78, 71, 0, 0 }, { 76, 79, 85, 0, 0, 0 }, { 76, 85, 0, 0, 0, 0 }, { 76, 85, 65, 78, 0, 0 }, { 76, 85, 69, 0, 0, 0 }, { 76, 85, 78, 0, 0, 0 }, { 76, 85, 79, 0, 0, 0 }, { 77, 0, 0, 0, 0, 0 }, { 77, 65, 0, 0, 0, 0 }, { 77, 65, 73, 0, 0, 0 }, { 77, 65, 78, 0, 0, 0 }, { 77, 65, 78, 71, 0, 0 }, { 77, 65, 79, 0, 0, 0 }, { 77, 69, 0, 0, 0, 0 }, { 77, 69, 73, 0, 0, 0 }, { 77, 69, 78, 0, 0, 0 }, { 77, 69, 78, 71, 0, 0 }, { 77, 73, 0, 0, 0, 0 }, { 77, 73, 65, 78, 0, 0 }, { 77, 73, 65, 79, 0, 0 }, { 77, 73, 69, 0, 0, 0 }, { 77, 73, 78, 0, 0, 0 }, { 77, 73, 78, 71, 0, 0 }, { 77, 73, 85, 0, 0, 0 }, { 77, 79, 0, 0, 0, 0 }, { 77, 79, 85, 0, 0, 0 }, { 77, 85, 0, 0, 0, 0 }, { 78, 0, 0, 0, 0, 0 }, { 78, 65, 0, 0, 0, 0 }, { 78, 65, 73, 0, 0, 0 }, { 78, 65, 78, 0, 0, 0 }, { 78, 65, 78, 71, 0, 0 }, { 78, 65, 79, 0, 0, 0 }, { 78, 69, 0, 0, 0, 0 }, { 78, 69, 73, 0, 0, 0 }, { 78, 69, 78, 0, 0, 0 }, { 78, 69, 78, 71, 0, 0 }, { 78, 73, 0, 0, 0, 0 }, { 78, 73, 65, 78, 0, 0 }, { 78, 73, 65, 78, 71, 0 }, { 78, 73, 65, 79, 0, 0 }, { 78, 73, 69, 0, 0, 0 }, { 78, 73, 78, 0, 0, 0 }, { 78, 73, 78, 71, 0, 0 }, { 78, 73, 85, 0, 0, 0 }, { 78, 79, 78, 71, 0, 0 }, { 78, 79, 85, 0, 0, 0 }, { 78, 85, 0, 0, 0, 0 }, { 78, 85, 65, 78, 0, 0 }, { 78, 85, 69, 0, 0, 0 }, { 78, 85, 78, 0, 0, 0 }, { 78, 85, 79, 0, 0, 0 }, { 79, 0, 0, 0, 0, 0 }, { 79, 85, 0, 0, 0, 0 }, { 80, 65, 0, 0, 0, 0 }, { 80, 65, 73, 0, 0, 0 }, { 80, 65, 78, 0, 0, 0 }, { 80, 65, 78, 71, 0, 0 }, { 80, 65, 79, 0, 0, 0 }, { 80, 69, 73, 0, 0, 0 }, { 80, 69, 78, 0, 0, 0 }, { 80, 69, 78, 71, 0, 0 }, { 80, 73, 0, 0, 0, 0 }, { 80, 73, 65, 78, 0, 0 }, { 80, 73, 65, 79, 0, 0 }, { 80, 73, 69, 0, 0, 0 }, { 80, 73, 78, 0, 0, 0 }, { 80, 73, 78, 71, 0, 0 }, { 80, 79, 0, 0, 0, 0 }, { 80, 79, 85, 0, 0, 0 }, { 80, 85, 0, 0, 0, 0 }, { 81, 73, 0, 0, 0, 0 }, { 81, 73, 65, 0, 0, 0 }, { 81, 73, 65, 78, 0, 0 }, { 81, 73, 65, 78, 71, 0 }, { 81, 73, 65, 79, 0, 0 }, { 81, 73, 69, 0, 0, 0 }, { 81, 73, 78, 0, 0, 0 }, { 81, 73, 78, 71, 0, 0 }, { 81, 73, 79, 78, 71, 0 }, { 81, 73, 85, 0, 0, 0 }, { 81, 85, 0, 0, 0, 0 }, { 81, 85, 65, 78, 0, 0 }, { 81, 85, 69, 0, 0, 0 }, { 81, 85, 78, 0, 0, 0 }, { 82, 65, 78, 0, 0, 0 }, { 82, 65, 78, 71, 0, 0 }, { 82, 65, 79, 0, 0, 0 }, { 82, 69, 0, 0, 0, 0 }, { 82, 69, 78, 0, 0, 0 }, { 82, 69, 78, 71, 0, 0 }, { 82, 73, 0, 0, 0, 0 }, { 82, 79, 78, 71, 0, 0 }, { 82, 79, 85, 0, 0, 0 }, { 82, 85, 0, 0, 0, 0 }, { 82, 85, 65, 0, 0, 0 }, { 82, 85, 65, 78, 0, 0 }, { 82, 85, 73, 0, 0, 0 }, { 82, 85, 78, 0, 0, 0 }, { 82, 85, 79, 0, 0, 0 }, { 83, 65, 0, 0, 0, 0 }, { 83, 65, 73, 0, 0, 0 }, { 83, 65, 78, 0, 0, 0 }, { 83, 65, 78, 71, 0, 0 }, { 83, 65, 79, 0, 0, 0 }, { 83, 69, 0, 0, 0, 0 }, { 83, 69, 78, 0, 0, 0 }, { 83, 69, 78, 71, 0, 0 }, { 83, 72, 65, 0, 0, 0 }, { 83, 72, 65, 73, 0, 0 }, { 83, 72, 65, 78, 0, 0 }, { 83, 72, 65, 78, 71, 0 }, { 83, 72, 65, 79, 0, 0 }, { 83, 72, 69, 0, 0, 0 }, { 83, 72, 69, 78, 0, 0 }, { 88, 73, 78, 0, 0, 0 }, { 83, 72, 69, 78, 0, 0 }, { 83, 72, 69, 78, 71, 0 }, { 83, 72, 73, 0, 0, 0 }, { 83, 72, 79, 85, 0, 0 }, { 83, 72, 85, 0, 0, 0 }, { 83, 72, 85, 65, 0, 0 }, { 83, 72, 85, 65, 73, 0 }, { 83, 72, 85, 65, 78, 0 }, { 83, 72, 85, 65, 78, 71 }, { 83, 72, 85, 73, 0, 0 }, { 83, 72, 85, 78, 0, 0 }, { 83, 72, 85, 79, 0, 0 }, { 83, 73, 0, 0, 0, 0 }, { 83, 79, 78, 71, 0, 0 }, { 83, 79, 85, 0, 0, 0 }, { 83, 85, 0, 0, 0, 0 }, { 83, 85, 65, 78, 0, 0 }, { 83, 85, 73, 0, 0, 0 }, { 83, 85, 78, 0, 0, 0 }, { 83, 85, 79, 0, 0, 0 }, { 84, 65, 0, 0, 0, 0 }, { 84, 65, 73, 0, 0, 0 }, { 84, 65, 78, 0, 0, 0 }, { 84, 65, 78, 71, 0, 0 }, { 84, 65, 79, 0, 0, 0 }, { 84, 69, 0, 0, 0, 0 }, { 84, 69, 78, 71, 0, 0 }, { 84, 73, 0, 0, 0, 0 }, { 84, 73, 65, 78, 0, 0 }, { 84, 73, 65, 79, 0, 0 }, { 84, 73, 69, 0, 0, 0 }, { 84, 73, 78, 71, 0, 0 }, { 84, 79, 78, 71, 0, 0 }, { 84, 79, 85, 0, 0, 0 }, { 84, 85, 0, 0, 0, 0 }, { 84, 85, 65, 78, 0, 0 }, { 84, 85, 73, 0, 0, 0 }, { 84, 85, 78, 0, 0, 0 }, { 84, 85, 79, 0, 0, 0 }, { 87, 65, 0, 0, 0, 0 }, { 87, 65, 73, 0, 0, 0 }, { 87, 65, 78, 0, 0, 0 }, { 87, 65, 78, 71, 0, 0 }, { 87, 69, 73, 0, 0, 0 }, { 87, 69, 78, 0, 0, 0 }, { 87, 69, 78, 71, 0, 0 }, { 87, 79, 0, 0, 0, 0 }, { 87, 85, 0, 0, 0, 0 }, { 88, 73, 0, 0, 0, 0 }, { 88, 73, 65, 0, 0, 0 }, { 88, 73, 65, 78, 0, 0 }, { 88, 73, 65, 78, 71, 0 }, { 88, 73, 65, 79, 0, 0 }, { 88, 73, 69, 0, 0, 0 }, { 88, 73, 78, 0, 0, 0 }, { 88, 73, 78, 71, 0, 0 }, { 88, 73, 79, 78, 71, 0 }, { 88, 73, 85, 0, 0, 0 }, { 88, 85, 0, 0, 0, 0 }, { 88, 85, 65, 78, 0, 0 }, { 88, 85, 69, 0, 0, 0 }, { 88, 85, 78, 0, 0, 0 }, { 89, 65, 0, 0, 0, 0 }, { 89, 65, 78, 0, 0, 0 }, { 89, 65, 78, 71, 0, 0 }, { 89, 65, 79, 0, 0, 0 }, { 89, 69, 0, 0, 0, 0 }, { 89, 73, 0, 0, 0, 0 }, { 89, 73, 78, 0, 0, 0 }, { 89, 73, 78, 71, 0, 0 }, { 89, 79, 0, 0, 0, 0 }, { 89, 79, 78, 71, 0, 0 }, { 89, 79, 85, 0, 0, 0 }, { 89, 85, 0, 0, 0, 0 }, { 89, 85, 65, 78, 0, 0 }, { 89, 85, 69, 0, 0, 0 }, { 89, 85, 78, 0, 0, 0 }, { 74, 85, 78, 0, 0, 0 }, { 89, 85, 78, 0, 0, 0 }, { 90, 65, 0, 0, 0, 0 }, { 90, 65, 73, 0, 0, 0 }, { 90, 65, 78, 0, 0, 0 }, { 90, 65, 78, 71, 0, 0 }, { 90, 65, 79, 0, 0, 0 }, { 90, 69, 0, 0, 0, 0 }, { 90, 69, 73, 0, 0, 0 }, { 90, 69, 78, 0, 0, 0 }, { 90, 69, 78, 71, 0, 0 }, { 90, 72, 65, 0, 0, 0 }, { 90, 72, 65, 73, 0, 0 }, { 90, 72, 65, 78, 0, 0 }, { 90, 72, 65, 78, 71, 0 }, { 67, 72, 65, 78, 71, 0 }, { 90, 72, 65, 78, 71, 0 }, { 90, 72, 65, 79, 0, 0 }, { 90, 72, 69, 0, 0, 0 }, { 90, 72, 69, 78, 0, 0 }, { 90, 72, 69, 78, 71, 0 }, { 90, 72, 73, 0, 0, 0 }, { 83, 72, 73, 0, 0, 0 }, { 90, 72, 73, 0, 0, 0 }, { 90, 72, 79, 78, 71, 0 }, { 90, 72, 79, 85, 0, 0 }, { 90, 72, 85, 0, 0, 0 }, { 90, 72, 85, 65, 0, 0 }, { 90, 72, 85, 65, 73, 0 }, { 90, 72, 85, 65, 78, 0 }, { 90, 72, 85, 65, 78, 71 }, { 90, 72, 85, 73, 0, 0 }, { 90, 72, 85, 78, 0, 0 }, { 90, 72, 85, 79, 0, 0 }, { 90, 73, 0, 0, 0, 0 }, { 90, 79, 78, 71, 0, 0 }, { 90, 79, 85, 0, 0, 0 }, { 90, 85, 0, 0, 0, 0 }, { 90, 85, 65, 78, 0, 0 }, { 90, 85, 73, 0, 0, 0 }, { 90, 85, 78, 0, 0, 0 }, { 90, 85, 79, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 83, 72, 65, 78, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };
  private static final String FIRST_PINYIN_UNIHAN = "阿";
  private static final String LAST_PINYIN_UNIHAN = "鿿";
  private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);
  private static HanziToPinyin sInstance;
  private final boolean mHasChinaCollator;

  protected HanziToPinyin(boolean paramBoolean)
  {
    this.mHasChinaCollator = paramBoolean;
  }

  public static HanziToPinyin getInstance()
  {
    synchronized (HanziToPinyin.class)
    {
      if (sInstance != null)
        return sInstance;
      Locale[] arrayOfLocale = Collator.getAvailableLocales();
      for (int i = 0; i < arrayOfLocale.length; i++)
        if ((arrayOfLocale[i].equals(Locale.CHINA)) || ((arrayOfLocale[i].getLanguage().equals("zh")) && (arrayOfLocale[i].getCountry().equals("HANS"))))
        {
          sInstance = new HanziToPinyin(true);
          return sInstance;
        }
      sInstance = new HanziToPinyin(true);
      return sInstance;
    }
  }

  private static boolean doSelfValidation()
  {
    char c1 = UNIHANS[0];
    Object localObject = Character.toString(c1);
    for (char c2 : UNIHANS)
      if (c1 != c2)
      {
        String str = Character.toString(c2);
        int k = COLLATOR.compare((String)localObject, str);
        if (k >= 0)
        {
          return false;
        }
        localObject = str;
      }
    return true;
  }

  private Token getToken(char paramChar)
  {
    Token localToken = new Token();
    String str1 = Character.toString(paramChar);
    localToken.source = str1;
    int i = -1;
    if (paramChar < 'Ā')
    {
      localToken.type = 1;
      localToken.target = str1;
      return localToken;
    }
    int j = COLLATOR.compare(str1, "阿");
    if (j < 0)
    {
      localToken.type = 3;
      localToken.target = str1;
      return localToken;
    }
    if (j == 0)
    {
      localToken.type = 2;
      i = 0;
    }
    else
    {
      j = COLLATOR.compare(str1, "鿿");
      if (j > 0)
      {
        localToken.type = 3;
        localToken.target = str1;
        return localToken;
      }
      if (j == 0)
      {
        localToken.type = 2;
        i = UNIHANS.length - 1;
      }
    }
    localToken.type = 2;
    if (i < 0)
    {
      int k = 0;
    int  m = UNIHANS.length - 1;
      while (k <= m)
      {
        i = (k + m) / 2;
        String str2 = Character.toString(UNIHANS[i]);
        j = COLLATOR.compare(str1, str2);
        if (j == 0)
          break;
        if (j > 0)
          k = i + 1;
        else
          m = i - 1;
      }
    }
    if (j < 0)
      i--;
    StringBuilder localStringBuilder = new StringBuilder();
    for (int m = 0; (m < PINYINS[i].length) && (PINYINS[i][m] != 0); m++)
      localStringBuilder.append((char)PINYINS[i][m]);
    localToken.target = localStringBuilder.toString();
    if (TextUtils.isEmpty(localToken.target))
    {
      localToken.type = 3;
      localToken.target = localToken.source;
    }
    return localToken;
  }

  public ArrayList<Token> get(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    if ((!this.mHasChinaCollator) || (TextUtils.isEmpty(paramString)))
      return localArrayList;
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder();
    int j = 1;
    for (int k = 0; k < i; k++)
    {
      int m = paramString.charAt(k);
      if (m == 32)
      {
        if (localStringBuilder.length() > 0)
          addToken(localStringBuilder, localArrayList, j);
      }
      else if (m < 256)
      {
        if ((j != 1) && (localStringBuilder.length() > 0))
          addToken(localStringBuilder, localArrayList, j);
        j = 1;
        localStringBuilder.append(m);
      }
      else
      {
        Token localToken = getToken((char) m);
        if (localToken.type == 2)
        {
          if (localStringBuilder.length() > 0)
            addToken(localStringBuilder, localArrayList, j);
          localArrayList.add(localToken);
          j = 2;
        }
        else
        {
          if ((j != localToken.type) && (localStringBuilder.length() > 0))
            addToken(localStringBuilder, localArrayList, j);
          j = localToken.type;
          localStringBuilder.append(m);
        }
      }
    }
    if (localStringBuilder.length() > 0)
      addToken(localStringBuilder, localArrayList, j);
    return localArrayList;
  }

  private void addToken(StringBuilder paramStringBuilder, ArrayList<Token> paramArrayList, int paramInt)
  {
    String str = paramStringBuilder.toString();
    paramArrayList.add(new Token(paramInt, str, str));
    paramStringBuilder.setLength(0);
  }

  public static class Token
  {
    public static final String SEPARATOR = " ";
    public static final int LATIN = 1;
    public static final int PINYIN = 2;
    public static final int UNKNOWN = 3;
    public int type;
    public String source;
    public String target;

    public Token()
    {
    }

    public Token(int paramInt, String paramString1, String paramString2)
    {
      this.type = paramInt;
      this.source = paramString1;
      this.target = paramString2;
    }
  }
}
