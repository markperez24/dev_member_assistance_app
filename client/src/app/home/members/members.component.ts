import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MemberService } from '../../shared/services/member.service';
import { MemberAssistanceApi } from '../../shared/constants/member.assistance.api';
import { MatTableDataSource } from '@angular/material/table';
import { DialogService } from '../../shared/services/dialog.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { FormControl } from '@angular/forms';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { MemberAssistanceMessages } from '../../shared/constants/member.assistance.messages';
import { isEqual } from 'lodash';
import { MemberListingModel } from '../../shared/model/member-listing.model';
import { NgxSpinnerService } from 'ngx-spinner';
import { processing } from '../../operators';
import { MemberAssistanceConstants } from '../../shared/constants/member.assistance.constants';

@Component({
  selector: 'app-members',
  templateUrl: './members.component.html',
  styleUrls: ['./members.component.scss']
})
export class MembersComponent implements OnInit {
  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string [] = ['fullName', 'idNumber', 'totalApplication', 'status', 'action'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  isLoading: boolean;
  private _members: MemberListingModel[];
  private subscription!: Subscription;
  searchField: FormControl;
  searchTextChanged = new Subject<string>();

  constructor(private memberService: MemberService,
              private dialogService: DialogService,
              private spinnerService: NgxSpinnerService) {
    this.isLoading = false;
    this._members = [];
    this.searchField = new FormControl();
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.paginator.pageSizeOptions = [10, 20, 50];
    this.paginator.pageSize = 20;
    this.dataSource.paginator = this.paginator;
    this.searchField.valueChanges
      .pipe(
        debounceTime(MemberAssistanceConstants.SEARCH_DELAY),
        distinctUntilChanged()
      ).subscribe(searched => {
        this.paginator.pageIndex = 0;
        this.displayMembers(searched);
    });
    this.displayMembers(this.searchField.value);
  }

  get members(): any[] {
    return this._members;
  }

  set members(value: any[]) {
    this._members = value;
  }

  private displayMembers(searched: string) {
    this.memberService.displayMembers(
      MemberAssistanceApi.GET_MEMBERS,
      {
        searchField: searched,
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize
      }
    ).pipe(processing(this.spinnerService))
      .subscribe(res => {
      this.isLoading = false;
      if (res && res.success) {
        this._members = res.memberList;
        //console.log('Members: ', this._members);
        this.dataSource = new MatTableDataSource<MemberListingModel>(this._members);
        this.paginator.length = res.total;
      } else if(res.error) {
        this.dialogService.open(
          {
            title: MemberAssistanceMessages.TITLE_WARNING_DISPLAY_MEMBERS,
            message: res.message
          }
        );
      }
    });
  }

  deny(member: MemberListingModel) {
      this.memberService.openDenyDialog();
      this.memberService.closeDenyDialog().subscribe(res=> {
        if(res) {
          member.reason = res.reason;
          this.memberService.denyApplication(
            MemberAssistanceApi.POST_DENY_APPLICATION,
            member
          ).subscribe(res => {
            if (res && res.success) {
              //update member
              this.displayMembers(this.searchField.value);
            }
          });
        }
      });
  }

  approve(member:any) {
    this.memberService.openApproveDialog(
      member
    );
    this.memberService.closeApproveDialog().subscribe(
      res => {
        if(res && res.success) {
          this.displayMembers(this.searchField.value);
        }
    });
  }

  isStatusProcessing(member: any) {
    if(isEqual(member.status, 'Processing')) {
      return true;
    } else {
      return false;
    }
  }

  onChangePage($event: PageEvent) {
    this.paginator.pageIndex = $event.pageIndex;
    this.paginator.pageSize = $event.pageSize;
    this.displayMembers(this.searchField.value)
  }
}
