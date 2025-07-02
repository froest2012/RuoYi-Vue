create table ry_function_matrix
(
    fm_id          bigint                             not null comment '功能矩阵ID'
        primary key,
    module         varchar(1024)                      not null comment '系统模块名称',
    function_desc  varchar(1024)                      not null comment '功能描述',
    demand_point   varchar(1024)                      not null comment '需求点说明',
    core_concepts  varchar(1024)                      null comment '核心概念解释',
    case_save_code varchar(255)                       not null comment '功能矩阵编码',
    prd            varchar(255)                       not null comment 'prd',
    user_id        varchar(255)                       null comment 'user_id',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    modify_time    datetime                           null comment '更新时间'
)
    comment '功能矩阵表';