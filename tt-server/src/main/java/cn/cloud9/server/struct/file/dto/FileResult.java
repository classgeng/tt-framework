package cn.cloud9.server.struct.file.dto;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.response.ApiResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月16日 上午 12:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("all")
public class FileResult extends ApiResult {

    public static FileResult fileError(ResultMessage errorMsg, Object ... args) {
        final FileResult result = new FileResult();
        result.message = MessageFormat.format(errorMsg.getMessage(), args);
        result.code = errorMsg.getCode();
        return result;
    }

    public static FileResult fileSuccess(List<FileDTO> fileInfo) {
        final FileResult result = new FileResult();
        final ResultMessage success = ResultMessage.SUCCESS;
        result.code = success.getCode();
        result.message = success.getMessage();
        result.data = fileInfo;
        return result;
    }
}
