package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    int getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

}

