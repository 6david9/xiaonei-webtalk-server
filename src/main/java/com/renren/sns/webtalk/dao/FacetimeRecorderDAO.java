package com.renren.sns.webtalk.dao;

import java.util.Date;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

import com.renren.sns.webtalk.models.FacetimeRecorder;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-3
 */
@DAO(catalog = "facetime_recorder")
public interface FacetimeRecorderDAO {

    @SQL("INSERT INTO facetime_recorder (caller_id, caller_rtmfp_stage, caller_rtmp_stage, caller_peer_id, receiver_id,receiver_rtmfp_stage, receiver_rtmp_stage, receiver_peer_id, talk_stage, state, caller_time,receiver_time,end_time ) "
            + "VALUES (:1, :2, :3, :4, :5, :6, :7, :8, :9, :10, :11, :12, :13)")
    public int insertRecorder(int caller_id, int caller_rtmfp_stage, int caller_rtmp_stage,
            String caller_peer_id, int receiver_id, int receiver_rtmfp_stage,
            int receiver_rtmp_stage, String receiver_peer_id, int talk_stage, int state,
            Date caller_time, Date receiver_time, Date end_time);

    @SQL("SELECT FROM facetime_recorder where caller_id = :1 limit 10")
    public List<FacetimeRecorder> selectById(int hostid);

}
/*
+----------------------+--------------+------+-----+---------------------+----------------+
| Field                | Type         | Null | Key | Default             | Extra          |
+----------------------+--------------+------+-----+---------------------+----------------+
| id                   | int(20)      | NO   | PRI | NULL                | auto_increment | 
| caller_id            | int(11)      | NO   | MUL | NULL                |                | 
| caller_rtmfp_stage   | tinyint(4)   | NO   |     | 0                   |                | 
| caller_peer_id       | varchar(256) | NO   |     |                     |                | 
| receiver_id          | int(11)      | NO   | MUL | NULL                |                | 
| receiver_peer_id     | varchar(256) | NO   |     |                     |                | 
| state                | tinyint(4)   | NO   | MUL | 0                   |                | 
| caller_time          | timestamp    | NO   | MUL | 0000-00-00 00:00:00 |                | 
| receiver_time        | timestamp    | NO   |     | 0000-00-00 00:00:00 |                | 
| end_time             | timestamp    | NO   |     | CURRENT_TIMESTAMP   |                | 
| caller_rtmp_stage    | tinyint(4)   | NO   |     | 0                   |                | 
| receiver_rtmfp_stage | tinyint(4)   | NO   |     | 0                   |                | 
| receiver_rtmp_stage  | tinyint(4)   | NO   |     | 0                   |                | 
| talk_stage           | tinyint(4)   | NO   | MUL | NULL                |                | 
+----------------------+--------------+------+-----+---------------------+----------------+*/
