package com.cikers.wechat.mall.modules.app.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.UserDao;
import com.cikers.wechat.mall.modules.app.entity.UserEntity;
import com.cikers.wechat.mall.modules.app.service.UserService;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import com.cikers.wechat.mall.modules.app.utils.HttpClientUtil;
import com.cikers.wechat.mall.modules.app.utils.StringTools;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Data
@Service("userService")
@Slf4j
@ConfigurationProperties(prefix = "cikers.wechat")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {




}
