import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentTextDetailComponent } from './document-text-detail.component';

describe('Component Tests', () => {
  describe('DocumentText Management Detail Component', () => {
    let comp: DocumentTextDetailComponent;
    let fixture: ComponentFixture<DocumentTextDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentTextDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentTextDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentTextDetailComponent);
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
