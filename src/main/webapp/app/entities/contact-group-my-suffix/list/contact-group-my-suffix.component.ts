import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactGroupMySuffix } from '../contact-group-my-suffix.model';
import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';
import { ContactGroupMySuffixDeleteDialogComponent } from '../delete/contact-group-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-contact-group-my-suffix',
  templateUrl: './contact-group-my-suffix.component.html',
})
export class ContactGroupMySuffixComponent implements OnInit {
  contactGroups?: IContactGroupMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactGroupService: ContactGroupMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.contactGroupService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IContactGroupMySuffix[]>) => {
            this.isLoading = false;
            this.contactGroups = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.contactGroupService.query().subscribe(
      (res: HttpResponse<IContactGroupMySuffix[]>) => {
        this.isLoading = false;
        this.contactGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IContactGroupMySuffix): number {
    return item.id!;
  }

  delete(contactGroup: IContactGroupMySuffix): void {
    const modalRef = this.modalService.open(ContactGroupMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactGroup = contactGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
