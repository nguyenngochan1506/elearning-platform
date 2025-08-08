package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserUpdateRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "USER-CONTROLLER", description = "User management operations")
public class UserController {
	private final UserService userService;
	@PostMapping("/list")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "permission.user.list",
			description = "List users with advanced filtering",
			extensions = {
					@Extension(name = "x-module", properties = {
							@ExtensionProperty(name = "value", value = "user")
					})
			}
	)
	public SuccessResponseDto<PageResponseDto<AdminUserResponse>> listUsers(@RequestBody @Valid AdvancedFilterRequestDto filter) {
		return SuccessResponseDto.<PageResponseDto<AdminUserResponse>>builder()
				.status(HttpStatus.OK.value())
				.message(Translator.translate("user.get.success"))
				.data(userService.listUsers(filter))
				.build();
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "permission.user.create",
			description = "Create a new user",
			extensions = {
					@Extension(name = "x-module", properties = {
							@ExtensionProperty(name = "value", value = "user")
					})
			}
	)
	public SuccessResponseDto<Long> createUser(@RequestBody @Valid AdminUserCreateRequestDto req) {
		return SuccessResponseDto.<Long>builder()
				.status(HttpStatus.CREATED.value())
				.message(Translator.translate("user.create.success"))
				.data(userService.createUser(req))
				.build();
	}
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
@Operation(summary = "permission.user.delete",
			description = "Delete a user by ID",
			extensions = {
					@Extension(name = "x-module", properties = {
							@ExtensionProperty(name = "value", value = "user")
					})
			}
	)
	public SuccessResponseDto<Long> deleteUser(@PathVariable Long id) {
		return SuccessResponseDto.<Long>builder()
				.status(HttpStatus.OK.value())
				.message(Translator.translate("user.delete.success"))
				.data(userService.deleteUser(id))
				.build();
	}
	@DeleteMapping("/batch")
	@ResponseStatus(HttpStatus.OK)
@Operation(summary = "permission.user.delete-many",
			description = "Delete multiple users by IDs",
			extensions = {
					@Extension(name = "x-module", properties = {
							@ExtensionProperty(name = "value", value = "user")
					})
			}
	)
	public SuccessResponseDto<Void> deleteUsers(@RequestBody UserManyDeleteRequestDto req) {
		userService.deleteManyUsers(req);
		return SuccessResponseDto.<Void>builder()
				.status(HttpStatus.OK.value())
				.message(Translator.translate("user.delete.success"))
				.build();
	}
	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "permission.user.update",
			description = "Update user details",
			extensions = {
					@Extension(name = "x-module", properties = {
							@ExtensionProperty(name = "value", value = "user")
					})
			}
	)
	public SuccessResponseDto<Long> updateUser(@RequestBody @Valid UserUpdateRequestDto req){
		return SuccessResponseDto.<Long>builder()
				.status(HttpStatus.OK.value())
				.message(Translator.translate("user.update.success"))
				.data(userService.updateUser(req))
				.build();
	}
}
