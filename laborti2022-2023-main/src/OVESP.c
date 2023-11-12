#include "OVESP.h"

/**
 * @file OVESP.c
 * @author Liwinux & Tinmar1010
 * @brief MEAN Online VEgetables Shopping Protocol function declarations
 * @version 0.1
 * @date 2023-10-01
 * 
 * @copyright Copyright (c) 2023
 * 
 */


/**
 * @brief Convert uint8_t* data to an OVESP structure with rows and columns.
 * Every time a row begins with the @param command, we know we begin a new row.
 * 
 * @param data : The source data to read from.
 * @return OVESP* : Pointer containing the converted data from the @param data pointer.
 * @return NULL : Malloc error occured.
 */
OVESP*OVESP_create_results(uint8_t *data);

/**
 * @brief Create a string seperate by a '#' for every token based on the OVESP structure.
 * This function doesn't add a '#' at the end of the string !
 * 
 * @param src_ovsp The source OVESP structure to read from.
 * @return char* : string containing all the data inside the OVESP separated by a '#' for every token.
 * @return NULL : Malloc error.
 */
char *OVESP_TOKENIZER(OVESP *src_ovsp);

/**
 * @brief Convert sql results to a OVESP structure
 * 
 * @param sql_results the sql results to convert.
 * @return OVESP* : Pointer containing the converted data from the sql results.
 * @return NULL : Malloc error occured.
 */
OVESP *OVESP_SQL_TO_OVESP(Sql_result *sql_results, const char *command);

Sql_result* OVESP_OVESP_TO_SQL(OVESP*ovesp_res);
/**
 * @brief Send an OVESP request in form of a string to a socket.
 * 
 * @param request The string to request to send.
 * @param dst_socket The destination socket to send the request to.
 * @return OVESP_DISCONNECT : If the socket has been disconnected. 
 * @return OVESP_INVALID_DATA : If a corrupted data has been passed.
 * @return OVESP_BROKEN_SOCK : If the data could no be received. That doesn't mean that the socket is closed or the connection is broken ! 
 */
static int OVESP_SEND(const char *request, int dst_socket);

/**
 * @brief Login main logic function.
 * 
 * @param request_tokens A pointer to pointer to OVESP struct that will hold all data from the request.
 * @param client_socket The client to try logging in.
 * @return 0 : If the request has successfully been sent.
 * @return OVESP_DISCONNECT : If the socket has been disconnected. 
 * @return OVESP_INVALID_DATA : If a corrupt data has been passed.
 * @return OVESP_BROKEN_SOCK : If the data could no be received. That doesn't mean that the socket is closed or the connection is broken !
 * 
 */
static int OVESP_LOGIN_OPERATION(OVESP *request_tokens, int client_socket);
static int OVESP_ACHAT_OPERATION(OVESP *request_tokens, int client_socket, OVESP **caddie);
static int OVESP_CADDIE_OPERATION(OVESP * caddie, int client_socket);
static int OVESP_UPDATE_CADDIE(OVESP *res, OVESP **caddie, const char *command);
static int OVESP_CANCEL_OPERATION(OVESP *request_tokens, int client_socket, OVESP **caddie);
static int OVESP_CONFIRMER_OPERATION(int client_socket, OVESP **caddie, char *username);
/**
 * @brief Receive a OVESP request and create an OVESP structure.
 * 
 * @param reply_tokens A pointer to pointer to OVESP struct that will hold all data from the request.
 * @param dst_socket The source socket to receive a OVESP request.
 * @return 0 : If the request has successfully been sent.
 * @return OVESP_DISCONNECT : If the socket has been disconnected. 
 * @return OVESP_INVALID_DATA : If the data is corruped or a malloc call failed.
 * @return OVESP_BROKEN_SOCK : If the data could no be received. That doesn't mean that the socket is closed or the connection is broken !
 * 
 */
static int OVESP_RECEIVE(OVESP **reply_tokens, int src_socket);

/**
 * @brief Deallocate memory for a pointer to OVESP struct.
 * 
 * @param ovesp The pointer to deallocate memory from.
 */
static void destroy_OVESP(OVESP *ovesp);


OVESP*OVESP_create_results(uint8_t *data)
{
    OVESP *ovesp_result;
    int i;
    int j;
    char *tmp_ptr;

    ovesp_result = (OVESP*)malloc(sizeof(OVESP));
    if (ovesp_result == NULL)
        return NULL;
    
    ovesp_result->data = NULL;
    tmp_ptr = strtok((char*)data, "#");
    if (tmp_ptr == NULL) /* If not found, bad request*/
        return NULL;
    
    ovesp_result->command = (char*)malloc(sizeof(char)*(strlen(tmp_ptr) + 1));
    if (ovesp_result->command == NULL)
        return NULL;
    
    strcpy(ovesp_result->command, tmp_ptr); /* Copy the command */
    ovesp_result->rows = 0;
    ovesp_result->columns_per_row = 0;

    /* Dont touch this, it works !!!!!!!!!!!!!!!!!! */

    for (i = 0; (tmp_ptr = strtok(NULL, "#")); i++) {
        ovesp_result->data = (char***)realloc(ovesp_result->data, sizeof(char**)*(i + 1));
        if (ovesp_result->data == NULL)
            return NULL;
        ovesp_result->data[i] = NULL;
        for (j = 0; tmp_ptr != NULL && strcmp(ovesp_result->command, tmp_ptr) != 0; tmp_ptr = strtok(NULL, "#"), j++) {
            ovesp_result->data[i] = (char**)realloc(ovesp_result->data[i],sizeof(char*)*(j + 1));
            if (ovesp_result->data[i] == NULL)
                return NULL;
            ovesp_result->data[i][j] = malloc(sizeof(char) * (strlen(tmp_ptr) + 1));
            if (ovesp_result->data[i][j] == NULL)
                return NULL;
            strcpy(ovesp_result->data[i][j], tmp_ptr);
        }
        ovesp_result->columns_per_row = j;
        ovesp_result->rows++;
    }

    return ovesp_result;

}

char *OVESP_TOKENIZER(OVESP *src_ovsp)
{
    char *res; /* Final result string */
    char *tmp_ptr; /* Temp variable to avoid overwriting the begining of the string */
    int i; /* Used as index */
    int j; /* Used as index */

    res = (char*)malloc(sizeof(char) *(strlen(src_ovsp->command) + 2));
    if (res == NULL)
        return NULL; 
    strcpy(res, src_ovsp->command);
    strcat(res, "#");
    tmp_ptr = res;


    for (i = 0; i < src_ovsp->rows; i++) { /* Loop through all rows */
        for (j = 0; j <src_ovsp->columns_per_row; j++) { /* Loop through all columns */

            /* Else, we can now safely use strlen */
            res = (char*)realloc(res, sizeof(char) * (strlen(src_ovsp->data[i][j]) + 2) + strlen(res));
            if (res == NULL)
                return NULL;

            tmp_ptr = res;
            tmp_ptr = res + strlen(res); /* If we don't do that, we overwrite the beginning of the string ...*/
            strcpy(tmp_ptr, src_ovsp->data[i][j]);
            strcat(tmp_ptr, "#");
            
        }
        if (src_ovsp->rows > 1 && i != src_ovsp->rows -1) {
            res = (char *)realloc(res, sizeof(char)* (strlen(res) + ((strlen(src_ovsp->command)) + 2)));
            tmp_ptr = res;
            tmp_ptr = res + strlen(res);
            strcat(tmp_ptr, src_ovsp->command);
            strcat(tmp_ptr, "#");
        }    
    }
    return res;
}

OVESP *OVESP_SQL_TO_OVESP(Sql_result *sql_results, const char *command)
{
    int i;
    int j;
    OVESP *ovesp_results;

    ovesp_results = (OVESP*)malloc(sizeof(OVESP));
    if (ovesp_results == NULL)
        return NULL;
    
    ovesp_results->rows = sql_results->rows;
    ovesp_results->columns_per_row = sql_results->columns_per_row;

    ovesp_results->command = (char*)malloc(sizeof(char) * strlen(command) + 1);
    if (ovesp_results->command == NULL)
        return NULL;
    
    strcpy(ovesp_results->command, command);

    ovesp_results->data = (char***)malloc(sizeof(char**)*ovesp_results->rows);
    if (ovesp_results->data == NULL)
        return NULL;
    
    for (i = 0; i < sql_results->rows; i++) {
        ovesp_results->data[i] = (char**)malloc(sizeof(char*)*ovesp_results->columns_per_row);
        if (ovesp_results->data[i] == NULL)
            return NULL;
        for(j = 0; j < sql_results->columns_per_row; j++) {
            ovesp_results->data[i][j] = (char*)malloc(sizeof(char)*(strlen(sql_results->array_request[i][j]) + 1));
            if (ovesp_results->data[i][j] == NULL)
                return NULL;
            strcpy(ovesp_results->data[i][j], sql_results->array_request[i][j]);
        }
    }

    return ovesp_results;
}

Sql_result* OVESP_OVESP_TO_SQL(OVESP*ovesp_res)
{
    int i;
    int j;
    Sql_result *sql_res;

    sql_res = (Sql_result*)malloc(sizeof(Sql_result));
    if (sql_res == NULL)
        return NULL;
    
    sql_res->rows = ovesp_res->rows;
    sql_res->columns_per_row = ovesp_res->columns_per_row;

    sql_res->array_request = (char***)malloc(sizeof(char**)*sql_res->rows);
    if (sql_res->array_request == NULL)
        return NULL;
    
    for (i = 0; i < ovesp_res->rows; i++) {
        sql_res->array_request[i] = (char**)malloc(sizeof(char*)*ovesp_res->columns_per_row);
        if (sql_res->array_request[i] == NULL)
            return NULL;
        for(j = 0; j < ovesp_res->columns_per_row; j++) {
            sql_res->array_request[i][j] = (char*)malloc(sizeof(char)*(strlen(ovesp_res->data[i][j]) + 1));
            if (sql_res->array_request[i][j] == NULL)
                return NULL;
            strcpy(sql_res->array_request[i][j], ovesp_res->data[i][j]);
        }
    }
    return sql_res;

}
int OVESP_RECEIVE(OVESP **reply_tokens, int src_socket)
{
    int error_check;
    Message *msg;

    msg = NULL;

    if ((error_check = Receive_msg(src_socket, &msg)) < 0)
        return error_check;
    
    *reply_tokens = OVESP_create_results(msg->data);
    /* If an error occured, destroy the message and return */
    if (*reply_tokens == NULL) {
        destroyMessage(msg);
        return -2;
    }

    /* Destroy the message, as we have tokenized it */
    destroyMessage(msg);
    
    return 0;
}
int OVESP_SEND(const char *request, int dst_socket)
{
    int error_check;

    error_check = 0;

    error_check = Send_msg(dst_socket, (uint8_t*)request, strlen(request) + 1);
    if (error_check > 0)
        return 0; /* Return 0 if successfull */

    return error_check;

}
int OVESP_LOGIN_OPERATION(OVESP *request_tokens, int client_socket)
{   
    int error_check;
    char buffer[200];


    error_check = 0;
    /* If new user is asked we check in the database if it already exists */
    if (strcmp(request_tokens->data[0][2], "1") == 0) {
        error_check = sql_create_new_user(request_tokens->data[0][0], request_tokens->data[0][1]);
        printf("%d\n", error_check);
        if (error_check == 1) {
            strcpy(buffer, LOGIN_FAIL(LOGIN_ALREADY_EXISTS));
        }
        else if (error_check == -1) {
            strcpy(buffer, LOGIN_FAIL(OVESP_DB_FAIL));
        }
        else if (error_check == 0) {
            strcpy(buffer, LOGIN_OK);
        }
    }
    else if (strcmp(request_tokens->data[0][2], "0") == 0) {
        error_check = sql_client_check_creds(request_tokens->data[0][0], request_tokens->data[0][1]);
        if (error_check == 0) {
            strcpy(buffer, LOGIN_OK);
        }
        else if (error_check == 1) {
            strcpy(buffer, LOGIN_FAIL(LOGIN_BAD_USER));
        }
        else if (error_check == 2) {
            strcpy(buffer, LOGIN_FAIL(LOGIN_BAD_PASSWORD));
        }
        else if (error_check == -1) {
            strcpy(buffer, LOGIN_FAIL(OVESP_DB_FAIL));
        }
    }
    else {
        printf("%s\n", request_tokens->data[0][2]);
        strcpy(buffer, LOGIN_FAIL(OVESP_BAD_REQUEST));
    }

    return OVESP_SEND(buffer, client_socket);

}
static int OVESP_CONSULT_OPERATION(OVESP *request_tokens, int client_socket)
{
    int error_check;
    char buffer_error[200];
    char *request_res;
    Sql_result *sql_res;
    OVESP *res;

    request_res = NULL;
    res = NULL;

    /* If IdArticle we return a bad request.*/
    if (check_is_number(request_tokens->data[0][0]) == 1) {
        
        sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, OVESP_BAD_REQUEST);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }

    else if ((error_check = sql_consult(request_tokens->data[0][0], &sql_res)) ==-1 || error_check == 1) {
         /* Database error */
        strcpy(buffer_error, CONSULT_FAIL);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else 
    {
        /* Trouve */
        if ((res = OVESP_SQL_TO_OVESP(sql_res, request_tokens->command)) == NULL)
        {
            sql_destroy_result(sql_res);
            strcpy(buffer_error, CONSULT_FAIL);
            error_check = OVESP_SEND(buffer_error, client_socket);
        }
        else
        {
            if ((request_res = OVESP_TOKENIZER(res)) == NULL) {
                destroy_OVESP(res);
                strcpy(buffer_error, CONSULT_FAIL);
                error_check = OVESP_SEND(buffer_error, client_socket);
            }
            else {
                error_check = OVESP_SEND(request_res, client_socket);
                destroy_OVESP(res) ;
                sql_destroy_result(sql_res);
                free(request_res);
            }
        }   
    }

    
    return error_check;
}
static int OVESP_ACHAT_OPERATION(OVESP *request_tokens, int client_socket, OVESP **caddie)
{   
    int error_check;
    char buffer_error[200];
    char *request_res;
    Sql_result *sql_res;
    OVESP *res;

    request_res = NULL;
    res = NULL;

    
    /* If IdArticle or quantity is not a number or quantity == 0, we return a bad request.*/
    if ((check_is_number(request_tokens->data[0][0]) == 1 || check_is_number(request_tokens->data[0][1]) == 1) ||
        (check_is_number(request_tokens->data[0][1]) == 0 && atoi(request_tokens->data[0][1]) == 0)) {
        
        sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, OVESP_BAD_REQUEST);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else if ((error_check = sql_achat(request_tokens->data[0][0],request_tokens->data[0][1], &sql_res)) == -1 ) {
         /* Database error */
        sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, OVESP_DB_FAIL);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else if (error_check == 1) {
        sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, ACHAT_FAIL);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else if (error_check == 2) {
        /* Stock insuffisant */
        sprintf(buffer_error, "%s#%s#%s", ACHAT_COMMAND, request_tokens->data[0][0], ACHAT_STOCK_INSUFFISANT);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else 
    {

        if ((res = OVESP_SQL_TO_OVESP(sql_res, request_tokens->command)) == NULL)
        {
            sql_destroy_result(sql_res);
            sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, ACHAT_FAIL);
            error_check = OVESP_SEND(buffer_error, client_socket);
        }
        else
        {
            if ((request_res = OVESP_TOKENIZER(res)) == NULL) {
                destroy_OVESP(res);
                sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, ACHAT_FAIL);
                error_check = OVESP_SEND(buffer_error, client_socket);
            }
            else {

                /* Update du caddie */
                if (*caddie == NULL) {
                    /* Creation du caddie*/
                    *caddie = OVESP_SQL_TO_OVESP(sql_res, CADDIE_COMMAND);
                    if (*caddie == NULL) {
                        sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, ACHAT_FAIL);
                        error_check = OVESP_SEND(buffer_error, client_socket);
                    }
                    
                    error_check = OVESP_SEND(request_res, client_socket);
                    destroy_OVESP(res) ;
                    sql_destroy_result(sql_res);
                    free(request_res);
                }
                else{
                    error_check = OVESP_UPDATE_CADDIE(res, caddie, ACHAT_COMMAND);
                    if (error_check == -1) {
                        sprintf(buffer_error, "%s#%s", ACHAT_COMMAND, ACHAT_FAIL);
                        error_check = OVESP_SEND(buffer_error, client_socket);
                    }
                    error_check = OVESP_SEND(request_res, client_socket);
                    destroy_OVESP(res) ;
                    sql_destroy_result(sql_res);
                    free(request_res);

                }
            }
        }   
    }
    return error_check;
}


int OVESP_CADDIE_OPERATION(OVESP * caddie, int client_socket)
{
    int error_check;
    char buffer_error[200];
    char *request_res;

    request_res = NULL;
    
    if (caddie == NULL) {
        sprintf(buffer_error, "%s#%s", CADDIE_COMMAND, CADDIE_EMPTY);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else if ((request_res = OVESP_TOKENIZER(caddie)) == NULL) {
        sprintf(buffer_error, "%s#%s", CADDIE_COMMAND, CADDIE_FAIL);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else {
        
        error_check = OVESP_SEND(request_res, client_socket);
        free(request_res);
    }
    return error_check;
}
int OVESP_CANCEL_OPERATION(OVESP *request_tokens, int client_socket, OVESP **caddie)
{
    int error_check;
    char buffer_error[200];
    char *request_res;

    
    request_res = NULL;
    error_check = 0;

    if ((error_check = sql_cancel(request_tokens->data[0][0],request_tokens->data[0][1])) == -1 ) {
         /* Database error */
        sprintf(buffer_error, "%s#%s", CANCEL_COMMAND, OVESP_DB_FAIL);
        error_check = OVESP_SEND(buffer_error, client_socket);
    }
    else
    {
        if ((request_res = OVESP_TOKENIZER(*caddie)) == NULL)
        {
            sprintf(buffer_error, "%s#%s", CANCEL_COMMAND, CANCEL_FAIL);
            error_check = OVESP_SEND(buffer_error, client_socket);
            return -1;
        }
        else
        {
            //printf("REPONSE : %s", request_res);
            /*Si le caddie n'existe pas alors on ne peut pas cancel car pas d'articles */
            if (*caddie == NULL)
            {
                sprintf(buffer_error, "%s#%s", CANCEL_COMMAND, CANCEL_FAIL);
                error_check = OVESP_SEND(buffer_error, client_socket);   
            }
            else
            {
                error_check = OVESP_UPDATE_CADDIE(request_tokens, caddie, CANCEL_COMMAND);
                if (error_check == -1) {
                    sprintf(buffer_error, "%s#%s", CANCEL_COMMAND, CANCEL_FAIL);
                    error_check = OVESP_SEND(buffer_error, client_socket);
                }
                error_check = OVESP_SEND(request_res, client_socket);
                free(request_res);
            }
        }
    }
    return error_check;
}
int OVESP_CONFIRMER_OPERATION(int client_socket, OVESP **caddie, char *username)
{
    int error_check;
    char buffer_error[200];
    Sql_result *sql_res;
    
    error_check = 0;

    if (*caddie == NULL)
    {
        sprintf(buffer_error, "%s#%s", CONFIRMER_COMMAND, CONFIRMER_FAIL);
        error_check = OVESP_SEND(buffer_error, client_socket);   
    }
    else
    {

        /*Fonction pour passer de OVESP à SQl*/
        sql_res = OVESP_OVESP_TO_SQL(*caddie);

        if ((error_check = sql_confirmer(&sql_res, username)) == -1 ) {
                /* Database error */
            sprintf(buffer_error, "%s#%s", CONFIRMER_COMMAND, OVESP_DB_FAIL);
            error_check = OVESP_SEND(buffer_error, client_socket);
        }
        else
        {
            /*Mettre le caddie à 0*/
            sprintf(buffer_error, "%s", CONFIRMER_SUCCESSFULL);
            error_check = OVESP_SEND(buffer_error, client_socket);

            /* Destroy bucket*/
            destroy_OVESP(*caddie);
            *caddie = NULL;
        }
    }
    return error_check;
}

/**
 * @brief Main server function to handle requests
 * 
 * @param client_socket The client socket to receive requests from
 * @return 0 : If the request has successfully been sent.
 * @return OVESP_DISCONNECT : If the socket has been disconnected. 
 * @return OVESP_INVALID_DATA : If a corrupt data has been passed.
 * @return OVESP_BROKEN_SOCK : If the data could no be received. That doesn't mean that the socket is closed or the connection is broken !
 */
int OVESP_server(int client_socket, OVESP **caddie)
{
    OVESP* request;
    int error_check;
    error_check = 0;

    error_check = OVESP_RECEIVE(&request, client_socket);
    if (error_check < 0)
        return error_check;


    if (strcmp(request->command, LOGIN_COMMAND) == 0) {
        error_check = OVESP_LOGIN_OPERATION(request, client_socket);
        if (error_check < 0) {
            destroy_OVESP(request);
            return error_check;
        }
    }

    else if (strcmp(request->command, CONSULT_COMMAND) == 0) {
        error_check = OVESP_CONSULT_OPERATION(request, client_socket);
        if (error_check < 0) {
            destroy_OVESP(request);
            return error_check;
        }
    }
    else if (strcmp(request->command, ACHAT_COMMAND) == 0) {
        error_check = OVESP_ACHAT_OPERATION(request, client_socket, caddie);
        if (error_check < 0) {
            destroy_OVESP(request);
            return error_check;
        }
    }
    else if (strcmp(request->command, CADDIE_COMMAND) == 0) {
        
        error_check = OVESP_CADDIE_OPERATION(*caddie, client_socket);
        if (error_check < 0) {
            destroy_OVESP(request);
            return error_check;
        } 
    }
    else if (strcmp(request->command, CANCEL_COMMAND) == 0) {
        
        error_check = OVESP_CANCEL_OPERATION(request, client_socket, caddie);
        if (error_check < 0) {
            destroy_OVESP(request);
            return error_check;
        }
    }
    else if (strcmp(request->command, CONFIRMER_COMMAND) == 0) {
        
        error_check = OVESP_CONFIRMER_OPERATION(client_socket, caddie, request->data[0][0]);
        if (error_check < 0) {
            destroy_OVESP(request);
            return error_check;
        }
        
    }
    else {
        
    }
    destroy_OVESP(request);

    return error_check;
}

int OVESP_Login(const char *user, const char *password, const char new_user_flag, int server_socket)
{
    int error_check;
    char buffer[200];
    OVESP *response;
    
    error_check = 0;

    sprintf(buffer, "%s#%s#%s#%c", LOGIN_COMMAND, user, password, new_user_flag + 0x30);

    error_check = OVESP_SEND(buffer, server_socket);
    /* if an error occured we return the return statement from the OVESP_SEND function */
    if (error_check < 0)
        return error_check;
    
    error_check = OVESP_RECEIVE(&response, server_socket);
    if (error_check < 0)
        return error_check;
    


    if (strcmp(response->command, LOGIN_COMMAND) == 0) {
        
        if (strcmp(response->data[0][0], OVESP_BAD_REQUEST) == 0) {
            destroy_OVESP(response);
            return 3;
        }

        else if (strcmp(response->data[0][0], SUCCESS) == 0) {
            destroy_OVESP(response);
            /* The login attempt has succeeded */
            return 0;
        }
        else if (strcmp(response->data[0][0], FAIL) == 0) {
            /* The login attempt has failed. Let's find out why ??? */
            if (strcmp(response->data[0][1], LOGIN_BAD_USER) == 0) {
                destroy_OVESP(response);
                return 1;
            }
            else if (strcmp(response->data[0][1], LOGIN_BAD_PASSWORD) == 0) {
                destroy_OVESP(response);
                return 2;
            }
            else if (strcmp(response->data[0][1], OVESP_DB_FAIL) == 0) {
                destroy_OVESP(response);
                return 3;
            }
            else if (strcmp(response->data[0][1], LOGIN_ALREADY_EXISTS) == 0) {
                destroy_OVESP(response);
                return 4;
            }
            else {
                destroy_OVESP(response);
                return -4; /* Bad reply from the server */
            }
        }  
        else {
            destroy_OVESP(response);
            return -4; /* Bad reply from the server */
        }
    }
    else {
        destroy_OVESP(response);
        return -4; /* Bad reply from the server */
    }
    
    destroy_OVESP(response);
    return 0;
}


/**
 * @brief Consult client main function logic. result will be allocate and must be freed after use
 * 
 * @param idArticle The id to look in the database.
 * @param server_socket The server socket to send the request to.
 * @param result contains the data after the operation. Must be freed afterward.
 * 
 * @return 0 : The operation has been successfull.
 * @return 1 : The article doesn't exists
 * @return -1 : If the connection with the socket is lost. Possibly client or server end.
 * @return -2 : If a malloc error occured or the data is corrupted.
 * @return -3 : If an I/O error occured. That doesn't mean that the socket is closed or the connection is broken !
 * @return -4 : If the server sent a bad reply. Could be a memory error from the server.
 */
int OVESP_Consult(int idArticle, int server_socket, OVESP **result)
{
    int error_check;
    char buffer[50];
    OVESP *response;
    error_check = 0;

    sprintf(buffer, "%s#%d", CONSULT_COMMAND, idArticle);


    error_check = OVESP_SEND(buffer, server_socket);
    /* if an error occured we return the return statement from the OVESP_SEND function */
    if (error_check < 0)
        return error_check;


    error_check = OVESP_RECEIVE(&response, server_socket);
    if (error_check < 0)
        return error_check;

    if(strcmp(response->command, CONSULT_COMMAND) == 0)
    {
        if(strcmp(response->data[0][0], "-1" ) == 0)
        {
            destroy_OVESP(response);
            return 1;
        }
        else
        {
            *result = response;
            return 0;   
        }
    }
    else {
        return -1;
    }
}
int OVESP_Achat(int idArticle, int quantite, int server_socket, OVESP **result)
{
    int error_check;
    char buffer[50];
    OVESP *ovesp;
    error_check = 0;

    sprintf(buffer, "%s#%d#%d#", ACHAT_COMMAND, idArticle, quantite);


    error_check = OVESP_SEND(buffer, server_socket);
    /* if an error occured we return the return statement from the OVESP_SEND function */
    if (error_check < 0)
        return error_check;


    error_check = OVESP_RECEIVE(&ovesp, server_socket);
    if (error_check < 0)
        return error_check;

    printf("%s\n", ovesp->data[0][0]);

    if(strcmp(ovesp->command, ACHAT_COMMAND) == 0)
    {
        if (strcmp(ovesp->data[0][0], OVESP_BAD_REQUEST) == 0) {
            destroy_OVESP(ovesp);
            return 3;
        }
        if(strcmp(ovesp->data[0][0], "-1" ) == 0)
        {
            destroy_OVESP(ovesp);
            return 1;
        }
        else if(strcmp(ovesp->data[0][1], "0" ) == 0)
        {
            destroy_OVESP(ovesp);
            return 2;
        }        
        else
        {
            *result = ovesp;  
          
        }
    }
    else {
        return -1;
    }

    return 0;
}

/**
 * @brief 
 * 
 * @param server_socket 
 * @param result 
 * @return 0 : Function execution successfull.
 * @return 1: If Caddie Error:
 * @return -1 : Bad response from the server. 
 */
int OVESP_Caddie(int server_socket, OVESP **result)
{
    int error_check;
    char buffer[50];
    OVESP *ovesp;
    error_check = 0;

    sprintf(buffer, "%s#", CADDIE_COMMAND);

    error_check = OVESP_SEND(buffer, server_socket);
    /* if an error occured we return the return statement from the OVESP_SEND function */
    if (error_check < 0)
        return error_check;


    error_check = OVESP_RECEIVE(&ovesp, server_socket);
    if (error_check < 0)
        return error_check;

    if(strcmp(ovesp->command, CADDIE_COMMAND) == 0)
    {
        if(strcmp(ovesp->data[0][0], "-1" ) == 0)
        {
            destroy_OVESP(ovesp);
            return 1;
        }   
        else if (strcmp(ovesp->data[0][0], "0") == 0) {
            destroy_OVESP(ovesp);
            return 2;
        }
        else
        {
            *result = ovesp;  
        }
    }
    else 
        return -1;
    
    return 0;
}
int OVESP_Cancel(char *idArticle, char *quantity, int server_socket)
{
    int error_check;
    char buffer[50];
    OVESP *ovesp;
    error_check = 0;

    sprintf(buffer, "%s#%s#%s#",CANCEL_COMMAND, idArticle,quantity);

    error_check = OVESP_SEND(buffer, server_socket);
    /* if an error occured we return the return statement from the OVESP_SEND function */
    if (error_check < 0)
        return error_check;


    error_check = OVESP_RECEIVE(&ovesp, server_socket);
    if (error_check < 0)
        return error_check;

    if(strcmp(ovesp->command, CANCEL_COMMAND) == 0)
    {
        if(strcmp(ovesp->data[0][0], "-1" ) == 0)
        {
            destroy_OVESP(ovesp);
            return 1;
        }   
    }
    else 
        return -1;
    
    return 0;   
}
int OVESP_Confirmer(int server_socket, char *username)
{
    int error_check;
    char buffer[50];
    OVESP *ovesp;
    error_check = 0;

    sprintf(buffer, "%s#%s",CONFIRMER_COMMAND, username);

    error_check = OVESP_SEND(buffer, server_socket);
    /* if an error occured we return the return statement from the OVESP_SEND function */
    if (error_check < 0)
        return error_check;


    error_check = OVESP_RECEIVE(&ovesp, server_socket);
    if (error_check < 0)
        return error_check;

    if(strcmp(ovesp->command, CONFIRMER_COMMAND) == 0)
    {
        if(strcmp(ovesp->data[0][0], "-1") == 0)
        {
            destroy_OVESP(ovesp);
            return 1;
        }   
    }
    else 
        return -1;
    
    return 0;

}
int OVESP_Cancel_All(int server_socket, OVESP **caddie)
{
    int i;
    int error_check = 0;
    for(i=0;i<(*caddie)->rows;i++)
        error_check = OVESP_Cancel((*caddie)->data[i][0], (*caddie)->data[i][3], server_socket);
    
    return error_check;
}
void destroy_OVESP(OVESP *ovesp)
{
    int i;
    int j;

    free(ovesp->command);
    for (i = 0; i < ovesp->rows; i++) {
        for (j = 0; j < ovesp->columns_per_row; j++) {
            free(ovesp->data[i][j]);
        }
        free(ovesp->data[i]);
        
    }
    free(ovesp->data);
    free(ovesp);
}

int OVESP_UPDATE_CADDIE(OVESP *res, OVESP **caddie, const char *command)
{
    int i;
    int j;
    int caddie_size;
    
    caddie_size = 0;
    

    if(strcmp(command, ACHAT_COMMAND) == 0)
    {
        for(i = 0; i < (*caddie)->rows; i++)
        {
            for(j = 0;j < (*caddie)->columns_per_row; j++)
            {
                caddie_size += strlen((*caddie)->data[i][j]) +1;
            }
        }

        (*caddie)->data = (char***)realloc((*caddie)->data, sizeof(char**) *((*caddie)->rows + 1) * (res->columns_per_row * caddie_size));
        if ((*caddie)->data == NULL)
            return -1;
        
        (*caddie)->data[(*caddie)->rows] = (char**)malloc(sizeof(char*) *(res->columns_per_row));
        if ((*caddie)->data[(*caddie)->rows] == NULL)
            return -1;
        
        
        for(i = 0;i<(*caddie)->columns_per_row;i++)
        {
            (*caddie)->data[(*caddie)->rows][i] = (char*)malloc(sizeof(char) * (strlen(res->data[0][i]) + 1));
            if ((*caddie)->data[(*caddie)->rows][i] == NULL)
                return -1;
            strcpy((*caddie)->data[(*caddie)->rows][i], res->data[0][i]);
            
        }
        (*caddie)->rows++;
    }
    else if(strcmp(command, CANCEL_COMMAND) == 0)
    {
        for(i = 0;i<(*caddie)->rows;i++)
        {
            if (strcmp((*caddie)->data[i][0], res->data[0][0]) == 0) {
                for (j = 0; j < (*caddie)->columns_per_row; j++)
                {
                    free((*caddie)->data[i][j]);  
                }
                free((*caddie)->data[i]);
                /* Decale tout ce qui se trouve apres*/
                for (j = i ; j < (*caddie)->rows; j++) {
                    if (j + 1 != (*caddie)->rows) {
                        (*caddie)->data[j] =  (*caddie)->data[j+1];
                        (*caddie)->data[j + 1] = NULL;
                    }

                }
                (*caddie)->rows--;
                if ((*caddie)->rows == 0) {
                    destroy_OVESP(*caddie);
                    *caddie = NULL;
                }
                return 0;
            }
        }

        return 1; /* Not found */
    }
        return 0;
}
