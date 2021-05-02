import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { HospitalService } from '../../../../shared/services/hospital.service';
import { MemberAssistanceApi } from '../../../../shared/constants/member.assistance.api';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog'; 
import { NgxSpinnerService } from 'ngx-spinner';
import { processing } from '../../../../operators';

@Component({
  selector: 'app-show-budget-history-dialog',
  templateUrl: './show-budget-history-dialog.component.html',
  styleUrls: ['./show-budget-history-dialog.component.scss']
})
export class ShowBudgetHistoryDialogComponent implements OnInit {
  displayedColumns: string [] = ['dateCreated', 'amount'];
  dataSource = new MatTableDataSource<any>([]);
  // get reference to paginator
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  short = 'shortDate';

  isLoading: boolean;
  hospitalId: any;
  hospitalName: any;
  dateCreated: any;
  amount: any;  

  constructor(private hospitalService: HospitalService,
              private spinnerService: NgxSpinnerService, 
              public dialogRef: MatDialogRef<ShowBudgetHistoryDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) { 
    this.isLoading = true;
    this.hospitalId = data.hospitalId;
    this.hospitalName = data.hospitalName;
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.paginator.pageSizeOptions = [5, 10, 20];
    this.paginator.pageSize = 5;
    this.dataSource.paginator = this.paginator;
    this.getBudgetHistory();
  }

  getBudgetHistory() {
    this.hospitalService.getHospitalDetails(
      MemberAssistanceApi.GET_HOSPITAL_BUDGET_HISTORY, {
        hospital : this.hospitalName
      }).pipe(processing(this.spinnerService))
      .subscribe(result => {
      this.isLoading = false;
      console.log(result);
      if (result && !result.error) {    
        this.dataSource.data = result.budgetHistoryList; 
        this.dataSource.paginator = this.paginator;
      } else {
        console.log(result.error);
      }       
    });
  }

  close() {
    this.dialogRef.close();
  }

}
