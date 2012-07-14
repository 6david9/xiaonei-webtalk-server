/**
 * 
 */

package com.renren.sns.webtalk.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;


/**
 * @author jinchao.wen
 * @email  jinchao.wen@opi-corp.com
 * @date   2011-3-17
 */
@DAO(catalog = "facetime_recorder")
public interface FacetimeFeedbackDAO {
    
    @SQL("INSERT INTO facetime_feedback (talk_type, type, detail) VALUES (:1, :2, :3)")
    public int insertFeedback(int talktype, int type, String detail);


}
