jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { InvoiceFaService } from '../service/invoice-fa.service';

import { InvoiceFaDeleteDialogComponent } from './invoice-fa-delete-dialog.component';

describe('Component Tests', () => {
  describe('InvoiceFa Management Delete Component', () => {
    let comp: InvoiceFaDeleteDialogComponent;
    let fixture: ComponentFixture<InvoiceFaDeleteDialogComponent>;
    let service: InvoiceFaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvoiceFaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(InvoiceFaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceFaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InvoiceFaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
