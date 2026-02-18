package com.newhorizon.nhapigateway.monolith;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static com.newhorizon.nhapigateway.monolith.BaseResponse.Status.FAILURE;
import static com.newhorizon.nhapigateway.monolith.BaseResponse.Status.SUCCESS;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ResponseCodesEnum {

    // OK response
    OK(0, "", HttpStatus.OK, SUCCESS),
    OK_EMPTY(1, "Empty response", HttpStatus.NO_CONTENT, SUCCESS),
    BAD_CREDENTIALS(-101, "Invalid credentials", HttpStatus.UNAUTHORIZED, FAILURE),
    INVALID_CLIENT_CONFIGURATION(-102, "Invalid auth client configuration", HttpStatus.SERVICE_UNAVAILABLE, FAILURE),
    UNHAUTORIZED_CLIENT(-103, "Auth client is not authorized to perform invoked operation", HttpStatus.INTERNAL_SERVER_ERROR, FAILURE),
    BAD_GRANT_TYPE(-104, "Unsupported grant type", HttpStatus.SERVICE_UNAVAILABLE, FAILURE),
    BAD_PARAMETER(-401, "Bad input parameters", HttpStatus.BAD_REQUEST, FAILURE),
    UNAUTHORIZED(-402, "Unauthorized", HttpStatus.UNAUTHORIZED, FAILURE),
    GENERIC_ERROR(-500, "Generic Error", HttpStatus.INTERNAL_SERVER_ERROR, FAILURE),
    NOT_IMPLEMENTED(-501, "Not Implemented", HttpStatus.NOT_IMPLEMENTED, FAILURE),
    DEPARTMENT_ALREADY_EXISTS(-600, "Department already exists", HttpStatus.BAD_REQUEST, FAILURE),
    ROOM_ALREADY_EXISTS(-601, "Room already exists", HttpStatus.BAD_REQUEST, FAILURE),
    DEPARTMENT_NOT_FOUND(-602, "Department not found", HttpStatus.NOT_FOUND, FAILURE),
    ROOM_NOT_FOUND(-603, "Room not found", HttpStatus.NOT_FOUND, FAILURE),
    BED_NOT_FOUND(-604, "Bed not found", HttpStatus.NOT_FOUND, FAILURE),
    PATIENT_NOT_FOUND(-605, "Patient not found", HttpStatus.NOT_FOUND, FAILURE),
    DEVICE_ALREADY_EXISTS(-606, "Device already exists", HttpStatus.BAD_REQUEST, FAILURE),
    DEVICE_NOT_FOUND(-607, "Device not found", HttpStatus.NOT_FOUND, FAILURE),
    DEVICE_NOT_ENABLED(-608, "Device not enabled", HttpStatus.BAD_REQUEST, FAILURE),
    DEVICE_ALREADY_ASSIGNED(-609, "Device already assigned", HttpStatus.BAD_REQUEST, FAILURE),
    DEVICE_NOT_ASSIGNED(-610, "Device is not assigned", HttpStatus.BAD_REQUEST, FAILURE),
    PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE(-611, "Provided patient is not assigned to given device", HttpStatus.BAD_REQUEST, FAILURE),
    BED_ALREADY_ASSIGNED(-612, "Bed already assigned", HttpStatus.BAD_REQUEST, FAILURE),
    PATIENT_NOT_ASSIGNED_TO_GIVEN_BED(-613, "Provided patient is not assigned to given bed", HttpStatus.BAD_REQUEST, FAILURE),
    BED_NOT_ASSIGNED(-614, "Bed is not assigned", HttpStatus.BAD_REQUEST, FAILURE),
    MEDICAL_CHART_OPENED_DETECTED(-615, "A medical chart open is detected", HttpStatus.BAD_REQUEST, FAILURE),
    EXAM_CODE_NOT_FOUND(-616, "Provided exam code do not exists", HttpStatus.BAD_REQUEST, FAILURE),
    MEDICAL_CHART_NOT_FOUND(-617, "Medical chart do not exists", HttpStatus.BAD_REQUEST, FAILURE),
    EXAM_NOT_ASSIGNED_TO_GIVEN_PATIENT(-618, "Provided exam is not assigned to given patient", HttpStatus.BAD_REQUEST, FAILURE),
    EXAM_NOT_REMOVABLE(-619, "Exam is in a state that can not allow the deletion", HttpStatus.BAD_REQUEST, FAILURE),
    ERROR_UPLOADING_EXAM_RESULT(-620, "Error occurred uploading exam result", HttpStatus.INTERNAL_SERVER_ERROR, FAILURE),
    EXAM_CODE_ALREADY_EXISTS(-621, "Exam with same code already exists", HttpStatus.BAD_REQUEST, FAILURE),
    MACHINE_ALREADY_EXISTS(-622, "Machine serial already exists", HttpStatus.BAD_REQUEST, FAILURE),
    MACHINE_NOT_FOUND(-623, "Machine not found", HttpStatus.BAD_REQUEST, FAILURE),
    SURGICAL_ROOM_ALREADY_EXISTS(-624, "Surgical Room already exists", HttpStatus.BAD_REQUEST, FAILURE),
    SURGICAL_ROOM_NOT_FOUND(-625, "Machine not found", HttpStatus.NOT_FOUND, FAILURE),
    SURGICAL_ROOM_TYPE_NOT_FOUND(-626, "Surgical Room Type not found", HttpStatus.NOT_FOUND, FAILURE),
    SURGERY_TYPE_ALREADY_EXISTS(-627, "Surgery Type already exists", HttpStatus.BAD_REQUEST, FAILURE),
    SURGERY_TYPE_NOT_FOUND(-628, "Surgery Type not found", HttpStatus.NOT_FOUND, FAILURE),
    SURGERY_START_AFTER_END(-629, "Surgery start cannot be after surgery end", HttpStatus.BAD_REQUEST, FAILURE),
    SCHEDULING_CONFLICT(-623, "Scheduling conflict", HttpStatus.CONFLICT, FAILURE),
    SCHEDULED_SURGERY_NOT_FOUND(-631, "Scheduled surgery not found", HttpStatus.NOT_FOUND, FAILURE),
    ACTIVE_INGREDIENT_ALREADY_EXISTS(-632, "Active Ingredient already exists", HttpStatus.BAD_REQUEST, FAILURE),
    DRUG_ALREADY_EXISTS(-633, "Drug already exists", HttpStatus.BAD_REQUEST, FAILURE),
    ASSOCIATION_ALREADY_EXISTS(-634, "Drug with ingredient association already exists", HttpStatus.BAD_REQUEST, FAILURE),
    DRUG_NOT_FOUND(-635, "Drug not found", HttpStatus.NOT_FOUND, FAILURE),
    DRUG_PACKAGE_ALREADY_EXISTS(-636, "Drug package already exists", HttpStatus.BAD_REQUEST, FAILURE),
    DRUG_PACKAGE_NOT_FOUND(-637, "Drug package not found", HttpStatus.NOT_FOUND, FAILURE),
    DRUG_QUANTITY_NOT_AVAILABLE(-638, "Drug quantity not available", HttpStatus.UNPROCESSABLE_ENTITY, FAILURE),
    NURSING_RATING_SCALE_CODE_NOT_FOUND(-639, "Nursing rating scale code not found", HttpStatus.NOT_FOUND, FAILURE),
    MEDICAL_MEMBER_ALREADY_EXISTS(-640, "Medical member already exists", HttpStatus.BAD_REQUEST, FAILURE),
    MEDICAL_MEMBER_NOT_FOUND(-641, "Medical member not found", HttpStatus.NOT_FOUND, FAILURE),
    ERROR_UPLOADING_ER_DOCUMENT(-642, "Error uploading Emergency Room Document", HttpStatus.INTERNAL_SERVER_ERROR, FAILURE),
    MEDICAL_CALENDAR_ALREADY_EXISTS(-643, "Medical calendar already exists", HttpStatus.BAD_REQUEST, FAILURE),
    MEDICAL_CALENDAR_NOT_FOUND(-644, "Medical calendar already exists", HttpStatus.NOT_FOUND, FAILURE),
    CALENDAR_DO_NOT_BELONG_TO_USER(-645, "Calendar do not belong to user", HttpStatus.BAD_REQUEST, FAILURE),
    WORK_SHIFT_CODE_NOT_FOUND(-646, "Work shift code not found", HttpStatus.NOT_FOUND, FAILURE),
    WORK_SHIFT_ASSOCIATION_NOT_FOUND(-647, "Work shift association not found", HttpStatus.NOT_FOUND, FAILURE),
    ADMINISTRATION_TYPE_CODE_NOT_FOUND(-648, "Administration type code not found", HttpStatus.NOT_FOUND, FAILURE),
    TREATMENT_ALREADY_IN_SUT(-649, "Treatment already in sut", HttpStatus.BAD_REQUEST, FAILURE),
    THERAPY_ENTRY_NOT_FOUND(-650, "therapy entry not found", HttpStatus.NOT_FOUND, FAILURE),
    ADMINISTRATION_STATUS_CODE_NOT_FOUND(-651, "Administration status code not found", HttpStatus.NOT_FOUND, FAILURE),
    ADMINISTRATION_NUMBER_REACHED(-652, "Administration number reached", HttpStatus.UNPROCESSABLE_ENTITY, FAILURE),
    PATIENT_ALREADY_EXISTS(-653, "Patient already exists", HttpStatus.BAD_REQUEST, FAILURE),
    GENDER_NOT_FOUND(-654, "Gender code not found", HttpStatus.NOT_FOUND, FAILURE),
    SUT_NOT_FOUND(-655, "SUT not found", HttpStatus.NOT_FOUND, FAILURE),
    ACTIVE_INGREDIENT_NOT_FOUND(-656, "Active Ingredient not found", HttpStatus.NOT_FOUND, FAILURE),
    PRESCRIBED_EXAM_NOT_FOUND(-657, "Prescribed exam not found", HttpStatus.NOT_FOUND, FAILURE);


    private final Integer errorCode;
    private final String description;
    private final HttpStatus httpErrorCode;
    private final BaseResponse.Status status;

    public static ResponseCodesEnum getResponseCodeByErrorCode(Integer errorCode) {
        Optional<ResponseCodesEnum> responseCode = Arrays.stream(ResponseCodesEnum.values())
                .filter(responseCodesEnum -> errorCode.equals(responseCodesEnum.getErrorCode()))
                .findFirst();
        return responseCode.orElse(null);
    }

}
