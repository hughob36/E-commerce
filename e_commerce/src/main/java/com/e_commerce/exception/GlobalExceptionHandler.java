package com.e_commerce.exception;

import com.e_commerce.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j //creo el log automatico con lombok
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Usamos warn porque es un error de flujo, no de código
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientStockException(InsufficientStockException ex) {

        log.warn("Items sin stock: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        // ERROR porque hubo un problema con la persistencia
        log.error("Violación de integridad de datos: ", ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Data invalid.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        // Usamos warn porque es un error de flujo, no de código
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Invalid username or password.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        // Logueamos el intento fallido (Nivel WARN)
        // Es útil para detectar ataques de fuerza bruta si ves muchos seguidos
        log.warn("Intento de inicio de sesión fallido: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Invalid username or password.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        // Esto se guarda en el archivo .log con fecha, hora y el error completo
        log.error("Error no controlado: ", ex);
        // Esto es lo que ve el usuario
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Something went wrong. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //este lanza el error 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        // El más importante: nos avisa de lo que no previmos
        log.error("ERROR CRÍTICO NO CONTROLADO: ", ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("An unexpected error occurred on the server.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //investigar
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        // Obtenemos el primer mensaje de error de las anotaciones (@NotBlank, etc.)
        // 1. Obtenemos el campo y el mensaje para un log útil
        String fieldName = ex.getBindingResult().getFieldErrors().get(0).getField();
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        // 2. Log de nivel WARN: Informa que hubo un rechazo por validación
        // Usamos {} para que sea eficiente
        log.warn("Validación fallida en el campo '{}': {}", fieldName, errorMessage);

        String fullMessage = fieldName + ": " + errorMessage;
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(fullMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //cuando no tengo un permiso
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AuthorizationDeniedException e) {
        // 1. Logueamos el intento de acceso no autorizado
        // Es vital para auditorias de seguridad
        // Obtenemos el nombre del usuario que esta logueado actualmente
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        log.warn("ACCESO DENEGADO: El usuario '{}' intentó acceder a un recurso sin permisos. Mensaje: {}",
                currentUser, e.getMessage());
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Access Denied: You do not have permission to perform this action.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDTO);
    }

}
