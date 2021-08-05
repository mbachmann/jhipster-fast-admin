import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactRelationMySuffix } from '../contact-relation-my-suffix.model';
import { ContactRelationMySuffixService } from '../service/contact-relation-my-suffix.service';
import { ContactRelationMySuffixDeleteDialogComponent } from '../delete/contact-relation-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-contact-relation-my-suffix',
  templateUrl: './contact-relation-my-suffix.component.html',
})
export class ContactRelationMySuffixComponent implements OnInit {
  contactRelations?: IContactRelationMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactRelationService: ContactRelationMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.contactRelationService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IContactRelationMySuffix[]>) => {
            this.isLoading = false;
            this.contactRelations = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.contactRelationService.query().subscribe(
      (res: HttpResponse<IContactRelationMySuffix[]>) => {
        this.isLoading = false;
        this.contactRelations = res.body ?? [];
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

  trackId(index: number, item: IContactRelationMySuffix): number {
    return item.id!;
  }

  delete(contactRelation: IContactRelationMySuffix): void {
    const modalRef = this.modalService.open(ContactRelationMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactRelation = contactRelation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
