package org.dromara.jpom.build.pipeline.model.config;

import lombok.Data;

import java.util.List;

/**
 * @author bwcx_jzy
 * @since 2024/4/8
 */
@Data
public class ArtifactItem {
    /**
     * 产物路径
     */
    private List<String> path;
    /**
     * 压缩格式，如果是目录情况
     */
    private CompressionFormat format;

    public enum CompressionFormat {
        /**
         * 压缩格式
         */
        ZIP,
        TAR_GZ,
    }
}
