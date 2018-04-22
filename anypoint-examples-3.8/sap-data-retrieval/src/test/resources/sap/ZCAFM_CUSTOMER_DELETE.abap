FUNCTION zcafm_customer_delete.
*"----------------------------------------------------------------------
*"*"Local Interface:
*"  IMPORTING
*"     VALUE(PI_CUSTOMER) TYPE  KNA1-KUNNR
*"     VALUE(PI_TEST) TYPE  CHAR1
*"  EXPORTING
*"     VALUE(SE_RETURN) TYPE  BAPIRET2
*"----------------------------------------------------------------------

*----------------------------------------------------------------------*
*  Function Module ZCAFM_CUSTOMER_DELETE *
* *----------------------------------------------------------------------*
* * *
* * ID-Reference: MuleSoft templates *
* * *
* * This BAPI aims to set deletion flag for particular customer. If par. *
* * PI_TEST is provided no change is performed. *
* *----------------------------------------------------------------------*
* * Change Log: *
* * *
* * Who Date Text *
* * MMARUSKIN 03.07.2014 Init creation. *
* *----------------------------------------------------------------------*

* Global data declarations


  CONSTANTS: lc_obj_task TYPE cmd_ei_object_task VALUE 'M', "M=Create/Change
             lc_msg_cls  TYPE sy-msgid VALUE 'ZMULESOFTINTEGRATION'.
  DATA: ls_customer  TYPE cmds_ei_extern,
        lt_customers TYPE cmds_ei_main,
        ls_mes_error TYPE cvis_message,
        lv_par       TYPE sy-msgv1.



*- Prepare data


  ls_customer-header-object_instance-kunnr = pi_customer.
  ls_customer-header-object_task = lc_obj_task.
  ls_customer-central_data-central-data-loevm = 'X'.
  ls_customer-central_data-central-datax-loevm = 'X'.
  APPEND ls_customer TO lt_customers-customers.
  lv_par = pi_customer.


*- Run change of material via standard functionality


  CALL METHOD cmd_ei_api=>maintain_bapi
    EXPORTING
      iv_test_run          = pi_test
      iv_collect_messages  = 'X'
      is_master_data       = lt_customers
    IMPORTING
      es_message_defective = ls_mes_error.


*- Error handling


  IF ls_mes_error-is_error IS INITIAL AND pi_test IS INITIAL.
    COMMIT WORK AND WAIT.
    CALL FUNCTION 'BALW_BAPIRETURN_GET2'     "okay: flag set!
      EXPORTING
        type      = 'I'
        cl        = lc_msg_cls
        number    = 100
        par1      = lv_par
        parameter = ''
        field     = ''
      IMPORTING
        return    = se_return.
  ELSE.
    ROLLBACK WORK.                                     "#EC CI_ROLLBACK
    CALL FUNCTION 'BALW_BAPIRETURN_GET2'     "problem: flag NOT set!
      EXPORTING
        type      = 'E'
        cl        = lc_msg_cls
        number    = 101
        par1      = lv_par
        parameter = ''
        field     = ''
      IMPORTING
        return    = se_return.
  ENDIF.

ENDFUNCTION.