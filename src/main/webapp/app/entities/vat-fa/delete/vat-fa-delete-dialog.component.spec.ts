jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { VatFaService } from '../service/vat-fa.service';

import { VatFaDeleteDialogComponent } from './vat-fa-delete-dialog.component';

describe('Component Tests', () => {
  describe('VatFa Management Delete Component', () => {
    let comp: VatFaDeleteDialogComponent;
    let fixture: ComponentFixture<VatFaDeleteDialogComponent>;
    let service: VatFaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VatFaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(VatFaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VatFaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(VatFaService);
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
