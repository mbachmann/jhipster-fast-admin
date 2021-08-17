import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentTextFaDetailComponent } from './document-text-fa-detail.component';

describe('Component Tests', () => {
  describe('DocumentTextFa Management Detail Component', () => {
    let comp: DocumentTextFaDetailComponent;
    let fixture: ComponentFixture<DocumentTextFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentTextFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentTextFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentTextFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentText on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentText).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
