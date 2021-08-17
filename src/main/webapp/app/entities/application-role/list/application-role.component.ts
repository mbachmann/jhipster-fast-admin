import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationRole } from '../application-role.model';
import { ApplicationRoleService } from '../service/application-role.service';
import { ApplicationRoleDeleteDialogComponent } from '../delete/application-role-delete-dialog.component';

@Component({
  selector: 'fa-application-role',
  templateUrl: './application-role.component.html',
})
export class ApplicationRoleComponent implements OnInit {
  applicationRoles?: IApplicationRole[];
  isLoading = false;

  constructor(protected applicationRoleService: ApplicationRoleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.applicationRoleService.query().subscribe(
      (res: HttpResponse<IApplicationRole[]>) => {
        this.isLoading = false;
        this.applicationRoles = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IApplicationRole): number {
    return item.id!;
  }

  delete(applicationRole: IApplicationRole): void {
    const modalRef = this.modalService.open(ApplicationRoleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicationRole = applicationRole;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
