import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceFaDetailComponent } from './invoice-fa-detail.component';

describe('Component Tests', () => {
  describe('InvoiceFa Management Detail Component', () => {
    let comp: InvoiceFaDetailComponent;
    let fixture: ComponentFixture<InvoiceFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InvoiceFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ invoice: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InvoiceFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load invoice on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.invoice).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
