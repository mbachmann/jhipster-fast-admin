import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactRelation } from '../contact-relation.model';
import { ContactRelationService } from '../service/contact-relation.service';
import { ContactRelationDeleteDialogComponent } from '../delete/contact-relation-delete-dialog.component';

@Component({
  selector: 'fa-contact-relation',
  templateUrl: './contact-relation.component.html',
})
export class ContactRelationComponent implements OnInit {
  contactRelations?: IContactRelation[];
  isLoading = false;

  constructor(protected contactRelationService: ContactRelationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.contactRelationService.query().subscribe(
      (res: HttpResponse<IContactRelation[]>) => {
        this.isLoading = false;
        this.contactRelations = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IContactRelation): number {
    return item.id!;
  }

  delete(contactRelation: IContactRelation): void {
    const modalRef = this.modalService.open(ContactRelationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactRelation = contactRelation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
