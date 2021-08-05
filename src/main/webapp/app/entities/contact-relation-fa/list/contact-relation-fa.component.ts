import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactRelationFa } from '../contact-relation-fa.model';
import { ContactRelationFaService } from '../service/contact-relation-fa.service';
import { ContactRelationFaDeleteDialogComponent } from '../delete/contact-relation-fa-delete-dialog.component';

@Component({
  selector: 'fa-contact-relation-fa',
  templateUrl: './contact-relation-fa.component.html',
})
export class ContactRelationFaComponent implements OnInit {
  contactRelations?: IContactRelationFa[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactRelationService: ContactRelationFaService,
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
          (res: HttpResponse<IContactRelationFa[]>) => {
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
      (res: HttpResponse<IContactRelationFa[]>) => {
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

  trackId(index: number, item: IContactRelationFa): number {
    return item.id!;
  }

  delete(contactRelation: IContactRelationFa): void {
    const modalRef = this.modalService.open(ContactRelationFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactRelation = contactRelation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
