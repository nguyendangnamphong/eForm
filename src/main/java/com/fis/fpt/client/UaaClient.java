package com.fis.fpt.client;

import com.fis.fpt.uaadomain.OrganizationDTO;
import com.fis.fpt.uaadomain.PersonalKycDetailProfileDTO;
import com.fis.fpt.uaadomain.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "uaaClient", url = "${application.uaa-name}")
public interface UaaClient {
    @GetMapping("/api/organizations/child/find-organization-like-orgin")
    public List<OrganizationDTO> findOrigin(@RequestParam(name = "orgIn") String orgIn);

    @GetMapping(value = "/api/users/login")
    public UserDTO getInfo(@RequestParam(name = "login") String login);

    @GetMapping(value = "/api/personal-kyc-detail-profiles-eaccount-by-userId/{id}")
    public PersonalKycDetailProfileDTO getInfoId(@PathVariable(name = "id") Long id);

}
