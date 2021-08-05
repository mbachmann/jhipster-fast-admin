import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactGroupFa } from '../contact-group-fa.model';
import { ContactGroupFaService } from '../service/contact-group-fa.service';
import { ContactGroupFaDeleteDialogComponent } from '../delete/contact-group-fa-delete-dialog.component';

@Component({
  selector: 'fa-contact-group-fa',
  templateUrl: './contact-group-fa.component.html',
})
export class ContactGroupFaComponent implements OnInit {
  contactGroups?: IContactGroupFa[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactGroupService: ContactGroupFaService,
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
          (res: HttpResponse<IContactGroupFa[]>) => {
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
      (res: HttpResponse<IContactGroupFa[]>) => {
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

  trackId(index: number, item: IContactGroupFa): number {
    return item.id!;
  }

  delete(contactGroup: IContactGroupFa): void {
    const modalRef = this.modalService.open(ContactGroupFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactGroup = contactGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
