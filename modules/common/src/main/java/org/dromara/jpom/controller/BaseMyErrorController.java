/*
 * Copyright (c) 2019 Of Him Code Technology Studio
 * Jpom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * 			http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package org.dromara.jpom.controller;

import cn.keepbx.jpom.model.JsonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bwcx_jzy
 * @see org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 * @since 2021/3/17
 */
@Slf4j
public abstract class BaseMyErrorController extends AbstractErrorController {

    public static final String FILE_MAX_SIZE_MSG = "上传文件太大了,请重新选择一个较小的文件上传吧";

    public BaseMyErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        // 判断异常信息
        Object attribute = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Map<String, Object> body = new HashMap<>(5);
        body.put(JsonMessage.CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        String msg = "啊哦，好像哪里出错了，请稍候再试试吧~";
        if (attribute instanceof MaxUploadSizeExceededException) {
            // 上传文件大小异常
            msg = FILE_MAX_SIZE_MSG;
            log.error("发生文件上传异常：{}  {}", statusCode, requestUri);
        } else if (status == HttpStatus.NOT_FOUND) {
            msg = "没有找到对应的资源";
            body.put(JsonMessage.DATA, requestUri);
        } else {
            log.error("发生异常：{}  {}", statusCode, requestUri);
        }
        body.put(JsonMessage.MSG, msg);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
