import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactPerson } from '../contact-person.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'fa-contact-person-detail',
  templateUrl: './contact-person-detail.component.html',
})
export class ContactPersonDetailComponent implements OnInit {
  contactPerson: IContactPerson | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactPerson }) => {
      this.contactPerson = contactPerson;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
