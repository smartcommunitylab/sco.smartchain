/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package it.smartcommunitylab.smartchainbackend.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;

import it.smartcommunitylab.smartchainbackend.service.UnauthorizedException;

@ControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
	public void handleIllegalArgument(HttpServletResponse res, Exception e) throws IOException {
		res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	}

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public void handleUnauthorized(HttpServletResponse res, Exception e) throws IOException {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
